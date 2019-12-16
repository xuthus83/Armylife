package cn.armylife.member.servicehysrix;

import cn.armylife.common.domain.Member;
import cn.armylife.member.feignservice.WechatPay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WechatPayHystrix implements WechatPay {
    Logger logger = LoggerFactory.getLogger(WechatPayHystrix.class);

    @Override
    public int WechatExtract(Member member,String orderfee){
        logger.info("payservice出错");
        return 0;
    };
}
