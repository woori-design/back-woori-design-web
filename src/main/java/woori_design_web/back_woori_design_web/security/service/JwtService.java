package woori_design_web.back_woori_design_web.security.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woori_design_web.back_woori_design_web.entity.Role;

import java.util.Optional;

public interface JwtService {

    String createAccessToken(Long id);

    String createRefreshToken(Long id);

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken, Role role);

    Optional<String> extractRefreshToken(HttpServletRequest request);

    Optional<String> extractAccessToken(HttpServletRequest request);

    Optional<String> extractEmail(String accessToken);

    Optional<Long> extractId(String accessToken);

    boolean isTokenValid(String token);
}
