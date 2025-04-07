package woori_design_web.back_woori_design_web.security.jwt.service.sendmanger;

import org.springframework.stereotype.Component;



import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woori_design_web.back_woori_design_web.config.JwtConfig;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenSendManager {
	private final JwtConfig jwtConfig;

	public void addTokenCookie(HttpServletResponse response, String name, String value, String path, int maxAge,
		boolean httpOnly) {
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(httpOnly);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);

	}

}
