package cn.armylife.integralmall.Feign;

import cn.armylife.common.Domain.Alipay;
import cn.armylife.integralmall.FeignHystrix.payserviceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "payservice",fallback = payserviceHystrix.class )
public interface payservice {

    @RequestMapping(value = "alipayOrder",method = RequestMethod.GET)
    Alipay alipay(@RequestParam String WIDout_trade_no,@RequestParam String WIDsubject,@RequestParam String WIDtotal_amount,@RequestParam String WIDbody,@RequestParam String passback_params);

    @RequestMapping(value = "MemberWxpay/pay",method = RequestMethod.GET)
    Map<String,String> order(@RequestParam String orderDesc,@RequestParam String Total,@RequestParam String orderCardID,@RequestParam String attach);
}
