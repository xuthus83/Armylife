package cn.armylife.payments.feignservice;

import cn.armylife.common.domain.AfterOrder;
import cn.armylife.common.domain.ShopOrder;
import cn.armylife.payments.hystrix.MarketServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "marketservice",fallback = MarketServiceHystrix.class)
public interface MarketService {

    /**
     * 查询订单
     * @param orderIds
     * @return
     */
    @RequestMapping(value = "selectOrder",method = RequestMethod.POST,produces = "cn.armylife.common.domain.ShopOrder")
    ShopOrder selectOrder(@RequestParam Long orderIds);

    /**
     * 增加理发会员
     */
    @RequestMapping(value = "plusVipHairOrder",method = RequestMethod.GET)
    int plusVipHairOrder(@RequestParam String out_trade_no,@RequestParam Long memberId);

    /**
     * 查询
     * @param orderId
     * @return
     */
    @RequestMapping(value = "SelectAfterOrder",method = RequestMethod.GET)
    AfterOrder SelectAfterOrder(@RequestParam Long orderId);
    /**
     * 更新订单信息
     * @param shopOrder
     * @return
     */
    @RequestMapping(value = "updateShopOrder",method = RequestMethod.GET)
    int updateShopOrder(@RequestParam ShopOrder shopOrder);

    @RequestMapping(value = "updateHairAmount",method = RequestMethod.GET)
    int updateHairAmount(@RequestParam String total,@RequestParam Long memberId,@RequestParam Long paymentsId);


    @RequestMapping(value = "plusOrderPeoPle",method = RequestMethod.GET)
    int plusOrderPeoPle(@RequestParam int number,@RequestParam Long ordersId);
}


