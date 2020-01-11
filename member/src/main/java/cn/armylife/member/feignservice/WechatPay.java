package cn.armylife.member.feignservice;

import cn.armylife.member.servicehysrix.WechatPayHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "payservice",fallback = WechatPayHystrix.class )
public interface WechatPay {

    @RequestMapping(value = "/MemberWxpay/WechatExtract",method = RequestMethod.GET)
    int WechatExtract(@RequestParam String openid,@RequestParam BigDecimal reward,@RequestParam Long memberId, @RequestParam String orderfee);
}
