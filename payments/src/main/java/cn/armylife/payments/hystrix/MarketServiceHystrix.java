package cn.armylife.payments.hystrix;

import cn.armylife.common.domain.AfterOrder;
import cn.armylife.common.domain.ShopOrder;
import cn.armylife.payments.feignservice.MarketService;
import org.springframework.stereotype.Component;

@Component
public class MarketServiceHystrix implements MarketService {

    @Override
    public ShopOrder selectOrder(Long orderIds){
        ShopOrder shopOrder=new ShopOrder();
        return shopOrder;
    };

    @Override
    public int plusVipHairOrder(String out_trade_no,Long memberId){
        return 0;
    };

    @Override
    public AfterOrder SelectAfterOrder(Long orderId){
        return null;
    };

    /**
     * 更新订单信息
     * @param shopOrder
     * @return
     */
    @Override
    public int updateShopOrder(ShopOrder shopOrder){
        return 0;
    }

    @Override
    public int updateHairAmount(String total,Long memberId,Long paymentsId){
        return 0;
    }

    @Override
    public int plusOrderPeoPle(int number, Long ordersId){
        return 0;
    }
}