package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "MEMBER")
@AllArgsConstructor
@Builder
public class Member {

    /**
     * 멤버 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 사용자 비밀번호
     */
    @Column(name = "password")
    private String encryptedPassword;

    /**
     * 사용자 인증 제공자 (로컬/구글/카카오/네이버)
     */
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    /**
     * 사용자 이메일
     */
    private String email;

    /**
     * 사용자 역할 (USER/ADMIN)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * 사용자 상태
     */
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /**
     * 사용자가 밴 당했는지 여부
     */
    private boolean banned;

}