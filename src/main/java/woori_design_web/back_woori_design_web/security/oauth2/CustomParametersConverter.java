package woori_design_web.back_woori_design_web.security.oauth2;


import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public class CustomParametersConverter
		implements Converter<OAuth2AuthorizationCodeGrantRequest, MultiValueMap<String, String>> {


	private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter =
			new OAuth2AuthorizationCodeGrantRequestEntityConverter();

	@Override
	public MultiValueMap<String, String> convert(OAuth2AuthorizationCodeGrantRequest request) {
		// 기본 Converter로 RequestEntity 생성
		RequestEntity<?> entity = defaultConverter.convert(request);

		// RequestEntity의 Body를 추출 -> MultiValueMap<String, String> 형태이면 그대로 반환
		if (entity != null && entity.getBody() instanceof MultiValueMap) {
			return (MultiValueMap<String, String>) entity.getBody();
		}

		// 만약 body가 없거나 MultiValueMap이 아닌 경우엔 빈 맵으로 반환
		return new LinkedMultiValueMap<>();
	}
}
