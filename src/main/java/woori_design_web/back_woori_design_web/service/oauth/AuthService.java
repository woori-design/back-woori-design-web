package woori_design_web.back_woori_design_web.service.oauth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.KakaoUserInfo;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.KakaoLoginResponse;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.OAuthTokenResponse;
import woori_design_web.back_woori_design_web.entity.*;
import woori_design_web.back_woori_design_web.repository.MemberRepository;
import woori_design_web.back_woori_design_web.repository.RefreshTokenRepository;
import woori_design_web.back_woori_design_web.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    /**
     * 카카오 로그인 처리
     * @param userInfo (id, properties(nickname), kakaoAccount(email)) 카카오 사용자 정보
     * @param refreshTokenExpiresIn 리프레시 토큰 만료 시간
     * @return KakaoLoginResponse (accessToken, refreshToken, isNewMember, memberId, name, email, provider, role, status)
     */
    @Transactional
    public KakaoLoginResponse processOAuthLogin(KakaoUserInfo userInfo, Integer refreshTokenExpiresIn) {
        String nickname = userInfo.properties().nickname();
        String email = userInfo.kakaoAccount().email();

        AtomicBoolean isNewMember = new AtomicBoolean(false);

        // 회원 확인 및 가입
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    isNewMember.set(true);
                    return registerNewMember(nickname, email);
                });

        // JWT 및 리프레시 토큰 생성
        String accessToken = jwtUtil.generateToken(member.getId(), member.getEmail(), member.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(member.getId(), refreshTokenExpiresIn);
        saveRefreshToken(member, refreshToken, refreshTokenExpiresIn);

        return KakaoLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewMember(isNewMember.get() ? "Y" : "N")
                .memberId(member.getId().toString())
                .name(member.getName())
                .email(member.getEmail())
                .provider(member.getProvider().name())
                .role(member.getRole().name())
                .status(member.getStatus().name())
                .build();
    }

    /**
     * 신규 회원 등록
     * @param nickname
     * @param email
     * @return Member
     */
    private Member registerNewMember(String nickname, String email) {
        Member newMember = Member.builder()
                .name(nickname)
                .email(email)
                .provider(SocialType.KAKAO)
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .banned(false)
                .build();
        memberRepository.save(newMember);

        log.info("신규 회원가입: {}", email);
        return newMember;
    }

    /**
     * 리프레시 토큰 저장
     * @param member
     * @param refreshToken
     * @param refreshTokenExpiresIn
     */
    private void saveRefreshToken(Member member, String refreshToken, Integer refreshTokenExpiresIn) {
        // 가장 최근의 기존 리프레시 토큰 조회
        Optional<RefreshToken> existingToken = refreshTokenRepository.findFirstByMemberOrderByCreatedAtDesc(member);

        // 토큰 유효성 검사 (유효한 토큰이 존재하면 만료 처리)
        if (existingToken.isPresent() && existingToken.get().isValid()) {
            existingToken.get().expire();
        }

        RefreshToken newToken = RefreshToken.builder()
                .member(member)
                .value(refreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiresIn))
                .isUsed(true)
                .build();
        refreshTokenRepository.save(newToken);
    }

    /**
     * 액세스 토큰 갱신
     * @param refreshToken
     * @return KakaoLoginResponse (accessToken, refreshToken, isNewMember, memberId, name, email, provider, role, status)
     */
    @Transactional
    public OAuthTokenResponse refreshAccessToken(String refreshToken) {
        // 리프레시 토큰 검증
        Claims claims = jwtUtil.validateRefreshToken(refreshToken);
        Long memberId = Long.valueOf(claims.getSubject());

        // DB에서 리프레시 토큰 확인
        RefreshToken storedToken = refreshTokenRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (!storedToken.getValue().equals(refreshToken) || !storedToken.isValid()) {
            throw new RuntimeException("Invalid refresh token");
        }

        // 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 새 토큰 발급
        String newAccessToken = jwtUtil.generateToken(member.getId(), member.getEmail(), member.getRole());
        String newRefreshToken = jwtUtil.generateRefreshToken(member.getId(),
                (int) java.time.Duration.between(LocalDateTime.now(), storedToken.getExpiresAt()).getSeconds());
                saveRefreshToken(member, newRefreshToken, (int) java.time.Duration.between(LocalDateTime.now(), storedToken.getExpiresAt()).getSeconds());

        return OAuthTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .isNewMember("N")
                .memberId(member.getId().toString())
                .name(member.getName())
                .email(member.getEmail())
                .provider(member.getProvider().name())
                .role(member.getRole().name())
                .status(member.getStatus().name())
                .build();
    }
}