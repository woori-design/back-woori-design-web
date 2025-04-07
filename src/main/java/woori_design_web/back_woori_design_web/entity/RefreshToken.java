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
@Table(name = "REFRESH_TOKEN")
public class RefreshToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // DB에서 자동으로 증가 (AUTO_INCREMENT)
    private Long id;

    // 토큰 소유자(회원)와 다대일(LAZY) 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 실제 토큰 값 (unique)
    @Column(name = "token_value", nullable = false, unique = true)
    private String value;

    // 만료 일시
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // 현재 사용 중인지 여부
    @Column
    private boolean isUsed;

    /**
     * 현재 유효한 토큰인지 검사
     */
    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiresAt);
    }

    /**
     * 토큰 만료 처리
     */
    public void expire() {
        this.expiresAt = LocalDateTime.now();
        this.isUsed = false;
    }

    /**
     * 새로운 값으로 토큰 업데이트
     * (간단 예시: 만료 시간을 7일 뒤로 갱신 + isUsed = true)
     */
    public void update(String newValue) {
        this.value = newValue;
        this.expiresAt = LocalDateTime.now().plusDays(7);
        this.isUsed = true;
    }

    protected void updateMember(Member member) {
        this.member = member;
    }
}

