package woori_design_web.back_woori_design_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woori_design_web.back_woori_design_web.entity.Comment;
import woori_design_web.back_woori_design_web.entity.Components;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByComponentsId(Long componentsId);
}
