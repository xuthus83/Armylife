package cn.armylife.market.feign;

import cn.armylife.common.domain.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "memberservice")
public interface MemberService {

    @RequestMapping(value = "selectMemberforId",method = RequestMethod.GET)
    Member selectMemberforId(@RequestParam Long memberId);

    @RequestMapping(value = "updateMember",method = RequestMethod.GET)
    int updateMember(@RequestParam Member member);

}
