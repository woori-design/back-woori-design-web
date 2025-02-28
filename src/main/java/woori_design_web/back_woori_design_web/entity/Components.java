package woori_design_web.back_woori_design_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "COMPONENTS")
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


    // authority
    @Column
    private Authority authority;


    /**
     * 빌드된 컴포넌트가 올려져 있는 s3 위치
     */
    @Column(columnDefinition = "TEXT")
    private String s3Url;

}