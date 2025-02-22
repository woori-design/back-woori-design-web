package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ComponentsOptionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 컴포넌트

    // 옵션 이름 - float button, 그냥 button
    private String name;

    // 타입...?
    // 여기엔 무엇을 넣지...?

    // 태그 엔티티는 또 사라졌네?

}
