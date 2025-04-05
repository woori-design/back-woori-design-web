package woori_design_web.back_woori_design_web.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import woori_design_web.back_woori_design_web.entity.Member;
import woori_design_web.back_woori_design_web.global.exception.BusinessLogicException;
import woori_design_web.back_woori_design_web.global.exception.ExceptionCode;
import woori_design_web.back_woori_design_web.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberServiceFacade {

    private final MemberRepository memberRepository;

    public Member getMemberInfo (Long id){
        return memberRepository.findById(id).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
        );
    }

    public Member getMemberInfoByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
        );
    }


}
