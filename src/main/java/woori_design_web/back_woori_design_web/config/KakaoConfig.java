package woori_design_web.back_woori_design_web.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KakaoConfig {
    @Value("${kakao.api.rest-api-key}")
    private String restApiKey;

    @Value("${kakao.api.redirect-uri}")
    private String redirectUri;

    private final String tokenUrl = "https://kauth.kakao.com/oauth/token";

    private final String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
}