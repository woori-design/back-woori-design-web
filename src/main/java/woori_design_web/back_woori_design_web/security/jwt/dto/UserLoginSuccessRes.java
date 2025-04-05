package woori_design_web.back_woori_design_web.security.jwt.dto;



import lombok.Builder;
import lombok.Getter;
import woori_design_web.back_woori_design_web.entity.Role;

@Getter
@Builder
public class UserLoginSuccessRes {
	private boolean success;
	private String message;
	private String accessToken;
	private String refreshToken;
	private Role role;
}
