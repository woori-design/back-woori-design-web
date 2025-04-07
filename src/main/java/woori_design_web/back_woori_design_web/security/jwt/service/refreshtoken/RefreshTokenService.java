package woori_design_web.back_woori_design_web.security.jwt.service.refreshtoken;

import woori_design_web.back_woori_design_web.entity.Member;

public interface RefreshTokenService {
    /**
     * DB에 RefreshToken 저장 (이미 존재하면 업데이트, 없으면 새로 생성)
     */
    void saveRefreshToken(Member member, String newRefreshToken);


}
