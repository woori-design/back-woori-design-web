package woori_design_web.back_woori_design_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woori_design_web.back_woori_design_web.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
