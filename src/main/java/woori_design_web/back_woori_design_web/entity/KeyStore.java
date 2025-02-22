package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    /**
     * API 키의 만료 시간
     */
    @Column(nullable = false)
    private LocalDateTime expirationTime;

    /**
     * API 키의 활성화 여부
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    /**
     * 키의 유효성을 검사하는 메서드
     *
     * @return 키 유효성 여부
     */
    public boolean isValid() {
        return isActive && LocalDateTime.now().isBefore(expirationTime);
    }

    /**
     * 키를 비활성화하는 메서드
     * <p>
     * 비활성화된 키는 즉시 사용이 중지되며, 배치 작업에 의해 물리적으로 삭제된다.
     */
    public void deactivate() {
        this.isActive = false;
    }

    protected void updateMember(Member member) {
        this.member = member;
    }

}
