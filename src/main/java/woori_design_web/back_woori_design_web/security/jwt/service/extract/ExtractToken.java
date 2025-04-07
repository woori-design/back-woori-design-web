package woori_design_web.back_woori_design_web.security.jwt.service.extract;



import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import static woori_design_web.back_woori_design_web.config.JwtConfig.BEARER;

@Component
public class ExtractToken {

	public Optional<String> extractTokenCookie(HttpServletRequest request, String tokenName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			return Arrays.stream(cookies)
				.filter(cookie -> tokenName.equals(cookie.getName()))
				.findFirst()
				.map(Cookie::getValue);
		}
		return Optional.empty();
	}

	public Optional<String> extractTokenHeader(HttpServletRequest request, String tokenName) {
		return Optional.ofNullable(request.getHeader(tokenName))
			.filter(verifyToken -> verifyToken.startsWith(BEARER))
			.map(verifyToken -> verifyToken.replace(BEARER, ""));
	}

}
