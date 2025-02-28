package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KEY_STORE")
public class KeyStore extends BaseTimeEntity {

    /**
     * API 키의 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * API 키를 소유한 회원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * API 키
     */
    @Column(nullable = false, unique = true)
    private String apiKey;

    /**
     * API 비밀키
     */
    @Column(nullable = false)
    private String apiSecretKey;



    protected void updateMember(Member member) {
        this.member = member;
    }

}
