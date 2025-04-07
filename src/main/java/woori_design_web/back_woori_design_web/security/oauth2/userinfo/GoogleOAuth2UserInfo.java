package woori_design_web.back_woori_design_web.security.oauth2.userinfo;

import java.util.Map;
import java.util.Optional;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return (String)attributes.get("sub");
	}

	@Override
	public String getNickName() {
		return (String)attributes.get("name");
	}

	@Override
	public String getEmail() {
		return Optional.ofNullable((String)attributes.get("email"))
			.map(email -> email + "_google")
			.orElse(null); // 여기에서는 null을 반환하지만, 다른 기본값으로 대체할 수도 있습니다.
	}

}
