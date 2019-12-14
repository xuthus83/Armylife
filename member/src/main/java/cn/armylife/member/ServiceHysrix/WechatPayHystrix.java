package cn.armylife.member.ServiceHysrix;

import cn.armylife.common.Domain.Member;
import cn.armylife.member.FeignService.WechatPay;
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
