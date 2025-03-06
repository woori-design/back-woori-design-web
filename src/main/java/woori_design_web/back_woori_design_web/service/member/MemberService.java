package woori_design_web.back_woori_design_web.service.member;

import woori_design_web.back_woori_design_web.service.member.dto.MemberInfoRes;

public interface MemberService {

    MemberInfoRes getUserInfoById(Long id);

}
