package woori_design_web.back_woori_design_web.security.service.create;

public interface JwtTokenGenerator {

    String generateAccessToken(String email, Long id);

    String generateRefreshToken();
}
