package woori_design_web.back_woori_design_web.security.jwt.service.create;



import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;


import lombok.RequiredArgsConstructor;
import woori_design_web.back_woori_design_web.config.JwtConfig;

import static woori_design_web.back_woori_design_web.config.JwtConfig.*;

@RequiredArgsConstructor
@Component
public class HmacJwtTokenGeneratorImpl implements JwtTokenGenerator {

	private final JwtConfig jwtConfig;

	@Override
	public String generateAccessToken(String email, Long id) {
		// 토큰 생성 로직
		Instant now = Instant.now();
		Instant expirationTime = now.plus(jwtConfig.getAccessTokenExpirationPeriod(), ChronoUnit.SECONDS);
		return JWT.create()
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(Date.from(expirationTime))
			.withClaim(EMAIL_CLAIM, email)
			.withClaim(USER_NUMBER, id)
			.sign(jwtConfig.getAlgorithm());
	}

	@Override
	public String generateRefreshToken(String email) {
		// 토큰 생성 로직
		Instant now = Instant.now();
		Instant expirationTime = now.plus(jwtConfig.getRefreshTokenExpirationPeriod(), ChronoUnit.SECONDS);
		return JWT.create()
				.withSubject(REFRESH_TOKEN_SUBJECT)
				.withClaim("email", email)                // 이메일 or 사용자 ID 등
				.withExpiresAt(Date.from(expirationTime))
				.withJWTId(UUID.randomUUID().toString())  // + 랜덤 jti
				.sign(jwtConfig.getAlgorithm());
	}
}
