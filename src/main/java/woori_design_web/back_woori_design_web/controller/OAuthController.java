package woori_design_web.back_woori_design_web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.OAuthTokenResponse;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.KakaoLoginResponse;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.KakaoTokenResponse;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.KakaoUserInfo;
import woori_design_web.back_woori_design_web.dto.common.ResponseDto;
import woori_design_web.back_woori_design_web.dto.common.ResponseDto.Status;
import woori_design_web.back_woori_design_web.service.oauth.AuthService;
import woori_design_web.back_woori_design_web.service.oauth.KakaoOAuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService;

    /**
     * 카카오 로그인
     * @param code 인가 코드
     * @return KakaoLoginResponse (accessToken, refreshToken, isNewMember, memberId, name, email, provider, role, status) 카카오 로그인 응답
     */
    @GetMapping("/oauth2/authorization/kakao")
    public ResponseEntity<ResponseDto<KakaoLoginResponse>> kakaoLogin(@RequestParam("code") String code) {
        try {
            // 카카오 토큰 발급
            KakaoTokenResponse tokenResponse = kakaoOAuthService.getTokens(code);

            // 카카오 사용자 정보 조회
            KakaoUserInfo userInfo = kakaoOAuthService.getUserInfo(tokenResponse.accessToken());

            // 로그인/회원가입 처리 및 토큰 발급
            KakaoLoginResponse response = authService.processOAuthLogin(userInfo, tokenResponse.refreshTokenExpiresIn());

            return new ResponseEntity<>(
                    new ResponseDto<>(Status.SUCCESS, "카카오 로그인 성공", response),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("카카오 로그인 처리 중 오류 발생: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseDto<>(Status.FAILURE, "카카오 로그인 실패", null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * 토큰 갱신
     * @param refreshToken 리프레시 토큰
     * @return OAuthTokenResponse (accessToken, refreshToken, isNewMember, memberId, name, email, provider, role, status) 토큰 갱신 응답
     */
    @PostMapping("/api/v1/refresh/token")
    public ResponseEntity<ResponseDto<OAuthTokenResponse>> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        try {
            OAuthTokenResponse response = authService.refreshAccessToken(refreshToken);
            return new ResponseEntity<>(
                    new ResponseDto<>(Status.SUCCESS, "토큰 갱신 성공", response),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            log.error("토큰 갱신 중 오류 발생: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseDto<>(Status.FAILURE, e.getMessage(), null),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}