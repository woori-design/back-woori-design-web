package woori_design_web.back_woori_design_web.service.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoRes {

    private String name;
    private String email;
}
