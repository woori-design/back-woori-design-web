package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COMMENT")
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;



    // 컴포넌트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "components_id")
    private Components components;

    /**
     * 댓글 내용
     */
    @Column(columnDefinition = "text")
    private String content;

}