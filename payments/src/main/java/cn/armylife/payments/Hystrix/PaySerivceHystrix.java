package cn.armylife.payments.Hystrix;

import cn.armylife.common.Domain.Member;
import cn.armylife.payments.Domain.Alipay;
import cn.armylife.payments.FeignService.Payservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaySerivceHystrix implements Payservice {
    Logger logger= LoggerFactory.getLogger(PaySerivceHystrix.class);

    @Override
    public Map<String, String> order(String orderDesc, String Total,String orderCardID,String attach) {
        Map<String, String> msg=new HashMap<>();
        msg.put("WechatPay,","Error");
        return msg;
    }

    @Override

   public int WechatExtract(Member member,String orderfee){
        return 0;
    };
    @Override
    public int transfer(BigDecimal amount,String identity, String order_title){
        logger.info("AliPay,Error");
        return 0;
    };

    @Override
    public int orderrefund(Long orderCardId, double refundfree,double totalfree,String desc){
        logger.info("Wechat,Error");
        return 0;
    };

    @Override
    public Alipay alipay(String WIDout_trade_no, String WIDsubject, String WIDtotal_amount, String WIDbody,String passback_params){
        return null;
    };
}
