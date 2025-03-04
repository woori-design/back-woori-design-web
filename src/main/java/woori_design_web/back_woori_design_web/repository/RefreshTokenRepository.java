package woori_design_web.back_woori_design_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findFirstByMemberOrderByCreatedAtDesc(Member member);
    Optional<RefreshToken> findFirstByMemberIdOrderByCreatedAtDesc(Long memberId);
}
