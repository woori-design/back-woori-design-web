package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 멤버

    // 컴포넌트

    /**
     * 댓글 내용
     */
    private String content;

}