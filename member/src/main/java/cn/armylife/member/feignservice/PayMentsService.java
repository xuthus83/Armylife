package cn.armylife.member.feignservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "paymentsservice")
public interface PayMentsService {

    @RequestMapping("Alipayrefund")
    int Alipayrefund(@RequestParam String WIDout_trade_no,
                     @RequestParam String WIDsubject,
                     @RequestParam String WIDrefund_amount,
                     @RequestParam String WIDbody);

    @RequestMapping("WechatPayTransfer")
    int WechatPayTransfer(@RequestParam Long orderCardId,
                          @RequestParam BigDecimal refundfree,
                          @RequestParam BigDecimal totalfree,
                          @RequestParam String desc);
}
