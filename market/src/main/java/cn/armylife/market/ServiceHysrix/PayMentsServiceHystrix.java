package cn.armylife.market.ServiceHysrix;

import cn.armylife.common.Domain.Payments;
import cn.armylife.market.Feign.PayMentsService;

import java.math.BigDecimal;

public class PayMentsServiceHystrix implements PayMentsService {

    @Override
    public int Alipayrefund(String WIDout_trade_no, String WIDsubject,String WIDrefund_amount,String WIDbody){
        return 0;
    };

    @Override
    public int WechatPayTransfer(Long orderCardId, BigDecimal refundfree, BigDecimal totalfree, String desc){
        return 0;
    }

    @Override
    public Payments selectPaments( Long orderId){
        return null;
    };
}
