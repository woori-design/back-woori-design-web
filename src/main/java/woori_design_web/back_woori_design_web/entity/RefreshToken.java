package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class RefreshToken {

    /**
     * 리프레쉬 토큰 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Member

    /**
     * 리프래쉬 토큰
     */
    private String refreshToken;

    /**
     * 리프레쉬 토큰 만료 시간
     */
    private Date refreshTokenExpiresAt;

}