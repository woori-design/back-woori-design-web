package woori_design_web.back_woori_design_web.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MEMBER")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자 이름
     */
    @Column
    private String name;

    /**
     * 사용자 비밀번호
     */
    @Column(name = "password")
    private String encryptedPassword;

    /**
     * 사용자 인증 제공자
     */
    @Column
    @Enumerated(EnumType.STRING)
    private SocialType provider;

    /**
     * 사용자 이메일
     */
    @Column
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
    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    /**
     * 사용자가 밴 당했는지 여부
     */
    @Column
    private boolean banned;

    /**
     * 삭제 일시
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * 사용자의 API 키 목록
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeyStore> keyStores = new ArrayList<>();

    /**
     * 사용자의 리프레시 토큰 목록
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> like = new ArrayList<>();



    /**
     * 사용자 API 키 추가 편의 메서드 (양방향 연관관계 설정)
     *
     * @param keyStore 추가할 API 키 정보
     */
    public void addKey(KeyStore keyStore) {
        this.keyStores.add(keyStore);
        keyStore.updateMember(this);
    }

    /**
     * 사용자 API 키 제거 편의 메서드 (양방향 연관관계 제거)
     *
     * @param keyStore 제거할 API 키 정보
     */
    public void removeKey(KeyStore keyStore) {
        this.keyStores.remove(keyStore);
        keyStore.updateMember(null);
    }

    /**
     * 리프레시 토큰 추가 편의 메서드 (양방향 연관관계 설정)
     *
     * @param refreshToken 추가할 리프레시 토큰
     */
    public void addRefreshToken(RefreshToken refreshToken) {
        this.refreshTokens.add(refreshToken);
        refreshToken.updateMember(this);
    }


}