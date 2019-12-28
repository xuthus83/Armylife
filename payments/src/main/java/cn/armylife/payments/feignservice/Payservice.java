package cn.armylife.payments.feignservice;

import cn.armylife.payments.domain.Alipay;
import cn.armylife.payments.hystrix.PaySerivceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@FeignClient(value = "payservice",fallback = PaySerivceHystrix.class)
public interface Payservice {

    @RequestMapping(value = "/MemberWxpay/pay",method = RequestMethod.GET)
    Map<String, String> order(@RequestParam String orderDesc, @RequestParam String Total, @RequestParam String orderCardID, @RequestParam String attach,@RequestParam String memberWechat,@RequestParam HttpServletRequest request);

    @RequestMapping(value = "/MemberWxpay/WechatExtract",method = RequestMethod.GET)
    int WechatExtract(@RequestParam String openid,@RequestParam BigDecimal reward,@RequestParam Long memberId,@RequestParam String orderfee);

    @RequestMapping(value = "/Alipayrefund",method = RequestMethod.POST)
    int Alipayrefund(@RequestParam String WIDout_trade_no,@RequestParam String WIDsubject, @RequestParam String WIDrefund_amount,@RequestParam String WIDbody);

    @RequestMapping(value = "/MemberWxpay/orderrefund",method = RequestMethod.POST)
    int orderrefund(@RequestParam Long orderCardId,@RequestParam double refundfree,@RequestParam double totalfree,@RequestParam String desc);

    @RequestMapping(value = "/alipayOrder",method = RequestMethod.GET)
    Alipay alipayOfOrder(@RequestParam String WIDout_trade_no,@RequestParam  String WIDsubject,@RequestParam  String WIDtotal_amount,@RequestParam  String WIDbody,@RequestParam String passback_params);
}
