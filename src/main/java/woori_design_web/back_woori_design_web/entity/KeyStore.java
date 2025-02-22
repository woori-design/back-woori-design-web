package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.Date;

public class KeyStore {

    /**
     * 발급된 키의 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 멤버

    /**
     * 발급된 api key
     */
    private String apiKey;

    /**
     * 발급된 api secret key
     */
    private String apiSecretKey;

    /**
     * 발급된 키의 만료 날짜
     */
    private Date expirationTime;

}
