package woori_design_web.back_woori_design_web.security.jwt.service.refreshtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.entity.RefreshToken;
import woori_design_web.back_woori_design_web.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * DB에 RefreshToken 저장 (이미 존재하면 업데이트, 없으면 새로 생성)
     */
    public void saveRefreshToken(Member member, String newRefreshToken) {
        // DB에서 "해당 멤버"의 RefreshToken이 있는지 조회
        Optional<RefreshToken> optionalToken = refreshTokenRepository.findByMember(member);

        // 존재하면 갱신, 없으면 생성
        RefreshToken token = optionalToken.map(existingToken -> {
            existingToken.update(newRefreshToken);  // value, 만료시간 등 업데이트
            return existingToken;
        }).orElseGet(() -> {
            // 새 토큰 엔티티 생성
            return RefreshToken.builder()
                    .member(member)
                    .value(newRefreshToken)
                    .expiresAt(LocalDateTime.now().plusDays(7)) // 예: 7일 후 만료
                    .isUsed(true)
                    .build();
        });

        refreshTokenRepository.save(token);
    }
}
