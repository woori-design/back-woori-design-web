package woori_design_web.back_woori_design_web.security.oauth2.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woori_design_web.back_woori_design_web.config.JwtConfig;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.entity.Role;
import woori_design_web.back_woori_design_web.security.jwt.service.JwtService;
import woori_design_web.back_woori_design_web.security.jwt.service.refreshtoken.RefreshTokenService;
import woori_design_web.back_woori_design_web.security.oauth2.CustomOAuth2User;
import woori_design_web.back_woori_design_web.service.member.MemberServiceFacade;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtService jwtService;
	private final JwtConfig jwtConfig;
	private final RefreshTokenService refreshTokenServiceImpl;  // DB에 RefreshToken을 저장하는 서비스
	private final MemberServiceFacade memberServiceFacade;      // Member 정보를 얻어오는 Facade/Service

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
										HttpServletResponse response,
										Authentication authentication)
			throws IOException, ServletException {

		try {
			// 소셜 로그인 인증이 완료된 사용자 정보
			CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

			// DB에서 Member 엔티티 조회 (없으면 예외 처리)
			Member member = memberServiceFacade.getMemberInfoByEmail(oAuth2User.getEmail());

			// 액세스/리프레시 토큰 발급 및 저장
			loginSuccess(response, member);



			response.sendRedirect(jwtConfig.getUserFrontendUrl());


		} catch (Exception e) {
			// 필요한 경우 커스텀 예외 처리 또는 로깅
			throw e;
		}
	}

	/**
	 * 액세스/리프레시 토큰을 발급하고, DB에 저장하는 메서드
	 */
	private void loginSuccess(HttpServletResponse response, Member member) throws IOException {
		// 1) 액세스 토큰 / 리프레시 토큰 생성
		String accessToken = jwtService.createAccessToken(member.getEmail());
		String refreshToken = jwtService.createRefreshToken(member.getEmail());

		// 2) 발급된 토큰을 응답에 담아 전송 (쿠키나 헤더 등 방식은 jwtService 내부 구현에 따라 다름)
		jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken, member.getRole());

		// 3) DB 테이블에 RefreshToken 저장 (혹은 갱신)
		refreshTokenServiceImpl.saveRefreshToken(member, refreshToken);
	}
}
