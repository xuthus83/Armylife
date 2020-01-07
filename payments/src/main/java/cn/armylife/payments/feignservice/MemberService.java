package cn.armylife.payments.feignservice;

import cn.armylife.common.domain.WXtemplate;
import cn.armylife.payments.hystrix.MemberServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "memberservice",fallback = MemberServiceHystrix.class)

public interface MemberService {

    @RequestMapping("newOrderService")
    String newOrderService(@RequestParam WXtemplate wXtemplate);

}
