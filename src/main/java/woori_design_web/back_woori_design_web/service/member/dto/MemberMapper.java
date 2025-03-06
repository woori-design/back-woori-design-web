package woori_design_web.back_woori_design_web.service.member.dto;

import org.springframework.stereotype.Component;
import woori_design_web.back_woori_design_web.entity.Member;

@Component
public class MemberMapper {

    public MemberInfoRes memberToMemberInfoRes(Member member) {
        return MemberInfoRes.builder()
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }


}
