package cn.armylife.payments.hystrix;

import cn.armylife.common.domain.WXtemplate;
import cn.armylife.payments.feignservice.MemberService;
import org.springframework.web.bind.annotation.RequestParam;

public class MemberServiceHystrix implements MemberService {

    public String newOrderService(@RequestParam WXtemplate wXtemplate){
        return "";
    };

}
