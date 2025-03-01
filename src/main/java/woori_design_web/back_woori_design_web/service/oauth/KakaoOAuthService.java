package woori_design_web.back_woori_design_web.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import woori_design_web.back_woori_design_web.config.KakaoConfig;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.KakaoTokenResponse;
import woori_design_web.back_woori_design_web.dto.oauth.OAuthDto.KakaoUserInfo;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate;

    /**
     * 카카오 토큰 발급
     * @param code 인가 코드
     * @return KakaoTokenResponse (accessToken, expiresIn, refreshToken, refreshTokenExpiresIn) 카카오 토큰 응답
     */
    public KakaoTokenResponse getTokens(String code) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", kakaoConfig.getRestApiKey());
        parameters.add("redirect_uri", kakaoConfig.getRedirectUri());
        parameters.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                kakaoConfig.getTokenUrl(), request, KakaoTokenResponse.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get tokens from Kakao");
        }
        return response.getBody();
    }

    /**
     * 카카오 사용자 정보 조회
     * @param accessToken 카카오 액세스 토큰
     * @return KakaoUserInfo (id, properties(nickname), kakaoAccount(email)) 카카오 사용자 정보
     */
    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                kakaoConfig.getUserInfoUrl(), HttpMethod.GET, request, KakaoUserInfo.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to get user info from Kakao");
        }
        return response.getBody();
    }
}