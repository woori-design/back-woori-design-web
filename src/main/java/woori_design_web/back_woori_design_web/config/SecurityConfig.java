package woori_design_web.back_woori_design_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults()) // 기본 CORS 설정 활성화
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (개발시)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/mypage",
                                "/my/*"
                        ).authenticated()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}