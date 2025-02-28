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
@Table(name = "REFRESH_TOKEN")
public class RefreshToken extends BaseTimeEntity {

    /**
     * 리프레시 토큰 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 토큰 소유자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 리프레시 토큰 값
     */
    @Column(name = "token_value", nullable = false, unique = true)
    private String value;

    /**
     * 리프레시 토큰 만료 시간
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * False : 사용 불가
     * True : 사용 가능
     */
    @Column
    private boolean isUsed;

    /**
     * 토큰의 유효성을 검사하는 메서드.
     * <p>
     * 1차적으로 토큰의 만료 시간을 기준으로 유효 여부를 판단하며,
     * 추가적으로 이전에 만료 처리 요청이 있었는지 확인하는 데 사용된다.
     *
     * @return 토큰이 유효한 경우 true, 그렇지 않은 경우 false
     */
    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiresAt);
    }

    /**
     * 토큰 만료 처리하는 메서드
     */
    public void expire() {
        this.expiresAt = LocalDateTime.now();
        this.isUsed = false;

    }

    protected void updateMember(Member member) {
        this.member = member;
    }
}