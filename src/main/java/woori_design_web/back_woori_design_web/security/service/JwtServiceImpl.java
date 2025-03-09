package woori_design_web.back_woori_design_web.security.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.entity.Role;
import woori_design_web.back_woori_design_web.service.member.MemberServiceFacade;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class JwtServiceImpl implements JwtService {

    private final JwtTokenizer jwtTokenizer;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtConfig jwtConfig;
    private final ExtractToken extractToken;
    private final MemberServiceFacade memberServiceFacade;


    @Override
    public String createAccessToken(Long id) {
        Member member = memberServiceFacade.getMemberInfo(id);
        return hmacJwtTokenGeneratorImpl.generateAccessToken(member.getId());
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
     */
    @Override
    public String createRefreshToken(Long id) {

        return hmacJwtTokenGeneratorImpl.generateRefreshToken();
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken,
                                          Role role) {

        jwtTokenizer.addAccessTokenCookie(response, accessToken);

        jwtTokenizer.addRefreshTokenCookie(response, refreshToken);

    }

    /**
     * RefreshToken 추출
     */
    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return extractToken.extractTokenCookie(request, jwtConfig.getRefreshTokenName());
    }

    /**
     * AccessToken 추출
     */
    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        // "Authorization" 헤더를 확인합니다.
        //String authorizationHeader = request.getHeader(AUTHORIZATION);
        // "Authorization" 헤더가 존재하면, 헤더에서 토큰을 추출합니다.
        //if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
        return extractToken.extractTokenHeader(request, AUTHORIZATION);
        //}

        // "Authorization" 헤더가 존재하지 않으면, 쿠키에서 토큰을 추출합니다.
        //return extractToken.extractTokenCookie(request, jwtConfig.getAccessTokenName());

    }

    /**
     * AccessToken에서 Email 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    @Override
    public Optional<String> extractEmail(String accessToken) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환

            return jwtTokenizer.verifyAccessToken(accessToken);
        } catch (Exception e) {

            return Optional.empty();
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            JWT.require(jwtConfig.getAlgorithm()).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

}
