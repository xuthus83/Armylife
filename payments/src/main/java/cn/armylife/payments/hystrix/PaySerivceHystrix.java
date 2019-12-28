package cn.armylife.payments.hystrix;

import cn.armylife.payments.domain.Alipay;
import cn.armylife.payments.feignservice.Payservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class PaySerivceHystrix implements Payservice {
    Logger logger= LoggerFactory.getLogger(PaySerivceHystrix.class);

    @Override
    public Map<String, String> order(String orderDesc, String Total, String orderCardID, String attach, String memberWechat, HttpServletRequest request) {
        Map<String, String> msg=new HashMap<>();
        msg.put("WechatPay,","Error");
        return msg;
    }

    @Override
    public int Alipayrefund(String WIDout_trade_no,String WIDsubject,String WIDrefund_amount,String WIDbody){
        return 0;
    };

    @Override
   public int WechatExtract(String openid,BigDecimal reward,Long memberId,String orderfee){
        return 0;
    };

    @Override
    public int orderrefund(Long orderCardId, double refundfree,double totalfree,String desc){
        logger.info("Wechat,Error");
        return 0;
    };

    @Override
    public Alipay alipayOfOrder(String WIDout_trade_no, String WIDsubject, String WIDtotal_amount, String WIDbody,String passback_params){
        Alipay alipay=new Alipay();
        return alipay;
    };
}
