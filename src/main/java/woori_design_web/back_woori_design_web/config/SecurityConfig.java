package woori_design_web.back_woori_design_web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import woori_design_web.back_woori_design_web.repository.MemberRepository;
import woori_design_web.back_woori_design_web.repository.RefreshTokenRepository;
import woori_design_web.back_woori_design_web.security.exception.CustomAuthenticationEntryPoint;

import woori_design_web.back_woori_design_web.security.jwt.filter.JwtAuthenticationProcessingFilter;
import woori_design_web.back_woori_design_web.security.jwt.service.JwtService;
import woori_design_web.back_woori_design_web.security.oauth2.CustomParametersConverter;
import woori_design_web.back_woori_design_web.security.oauth2.handler.OAuth2LoginFailureHandler;
import woori_design_web.back_woori_design_web.security.oauth2.handler.OAuth2LoginSuccessHandler;
import woori_design_web.back_woori_design_web.security.oauth2.service.CustomOAuth2UserService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OauthConfig oauthConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // [PART 1]
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // [PART 2]
                //== URL별 권한 관리 옵션 ==//
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(
                                "/jwt-test",
                                "/oauth2/**",
                                "/login",
                                "/api/v1/board/**",
                                "/api/v1/auth"

                        ).permitAll() // 해당 요청은 인증이 필요 없음
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated() // 해당 요청은 인증이 필요함
                )
                // [PART 3]
                //== 소셜 로그인 설정 ==//
                .oauth2Login(oauth2 -> oauth2
                        .tokenEndpoint(token -> token.accessTokenResponseClient(accessTokenResponseClient())) // 토큰 엔드포인트 설정
                        .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                        .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정

                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)))

                // Custom Exception Handling
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint(new ObjectMapper()))
                );

        // [PART4]
        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationProcessingFilter, LogoutFilter.class);

        return http.build();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    } // 패스워드 인코더

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        // TODO: 이 부분은 나중에 삭제해야 됨
        //configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP 메서드 허용
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 변경된 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    } // CORS 설정

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    } // 정적 리소스 보안 필터 해제





    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        // 구버전 RestClientAuthorizationCodeTokenResponseClient
        RestClientAuthorizationCodeTokenResponseClient client = new RestClientAuthorizationCodeTokenResponseClient();

        // 여기서 setRequestEntityConverter가 아닌 addParametersConverter 사용
        client.addParametersConverter(new CustomParametersConverter());

        return client;
    }
}