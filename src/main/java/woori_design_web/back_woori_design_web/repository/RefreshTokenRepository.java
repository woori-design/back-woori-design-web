package woori_design_web.back_woori_design_web.repository;

import org.apache.el.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findFirstByMemberOrderByCreatedAtDesc(Member member);
    Optional<RefreshToken> findFirstByMemberIdOrderByCreatedAtDesc(Long memberId);
    // 특정 Member의 리프레시 토큰 조회
    Optional<RefreshToken> findByMember(Member member);

    Optional<RefreshToken> findByValue(String value);
}
