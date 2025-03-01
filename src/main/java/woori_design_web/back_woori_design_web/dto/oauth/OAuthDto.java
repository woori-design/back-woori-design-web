package woori_design_web.back_woori_design_web.dto.oauth;

import lombok.Builder;

public record OAuthDto() {

    @Builder
    public record KakaoUserInfo(
            String id, // 카카오 사용자 고유 ID(회원 번호)
            Properties properties,
            KakaoAccount kakaoAccount
    ) {
        @Builder
        public record Properties(
                String nickname
        ) {}

        @Builder
        public record KakaoAccount(
                String email
        ) {}
    }

    @Builder
    public record KakaoTokenResponse(
            String accessToken,
            Integer expiresIn,
            String refreshToken,
            Integer refreshTokenExpiresIn
    ) {}

    @Builder
    public record KakaoLoginResponse (
            String accessToken,
            String refreshToken,
            String isNewMember,
            String memberId,
            String name,
            String email,
            String provider,
            String role,
            String status
    ) {}


}
