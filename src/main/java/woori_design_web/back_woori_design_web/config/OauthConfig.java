package woori_design_web.back_woori_design_web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Getter
public class OauthConfig {


	public static final String KAKAO_NAME = "kakao";

}
