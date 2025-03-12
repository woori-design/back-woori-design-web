package woori_design_web.back_woori_design_web.security.service.create;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class HmacJwtTokenGeneratorImpl implements JwtTokenGenerator{
    @Override
    public String generateAccessToken(String email, Long id) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(jwtConfig.getAccessTokenExpirationPeriod(), ChronoUnit.SECONDS);

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(Date.from(expirationTime))
                .withClaim(EMAIL, email)
                .withClaim(USER_NUMBER, id)
                .sign(jwtConfig.getAlgorithm());
    }

    @Override
    public String generateRefreshToken() {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(jwtConfig.getRefreshTokenExpirationPeriod(), ChronoUnit.SECONDS);
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(Date.from(expirationTime))
                .sign(jwtConfig.getAlgorithm());
    }


}
