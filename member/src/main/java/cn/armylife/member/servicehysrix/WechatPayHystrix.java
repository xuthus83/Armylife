package cn.armylife.member.servicehysrix;

import cn.armylife.member.feignservice.WechatPay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WechatPayHystrix implements WechatPay {
    Logger logger = LoggerFactory.getLogger(WechatPayHystrix.class);

    @Override
    public int WechatExtract(String openid, BigDecimal reward, Long memberId, String orderfee){
        logger.info("payservice出错");
        return 0;
    };
}
