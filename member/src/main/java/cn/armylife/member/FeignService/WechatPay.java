package cn.armylife.member.FeignService;

import cn.armylife.common.Domain.Member;
import cn.armylife.member.ServiceHysrix.WechatPayHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "payservice",fallback = WechatPayHystrix.class )
public interface WechatPay {

    @RequestMapping(value = "WechatExtract",method = RequestMethod.GET)
    int WechatExtract(@RequestParam Member member,@RequestParam String orderfee);
}
