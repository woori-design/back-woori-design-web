package woori_design_web.back_woori_design_web.service.member;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.repository.MemberRepository;
import woori_design_web.back_woori_design_web.service.member.dto.MemberInfoRes;
import woori_design_web.back_woori_design_web.service.member.dto.MemberMapper;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberServiceFacade memberServiceFacade;
    private final MemberMapper memberMapper;

    @Override
    public MemberInfoRes getUserInfoById(Long id){
        Member member = memberServiceFacade.getMemberInfo(id);
        return memberMapper.memberToMemberInfoRes(member);
    }



}
