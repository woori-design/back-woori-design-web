package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Components {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 컴포넌트 이름
     */
    @Column(name = "component_name")
    private String name;

    @Column(name = "component_description")
    private String description;

    // component_type
    // 컴포넌트 타입을 enum으로 관리할까?

    // authority
    // authority도 enum으로 관리?
    // 그럼 어떤 권한들을 만들꺼지?

    /**
     * 빌드된 컴포넌트가 올려져 있는 s3 위치
     */
    private String s3Url;

}