package cn.armylife.payments.FeignService;

import cn.armylife.common.Domain.AfterOrder;
import cn.armylife.payments.Hystrix.MarketServiceHystrix;
import cn.armylife.common.Domain.ShopOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "marketservice",fallback = MarketServiceHystrix.class)
public interface MarketService {

    @RequestMapping(value = "selectOrder",method = RequestMethod.GET)
    ShopOrder selectOrder(@RequestParam Long orderIds);

    @RequestMapping(value = "plusVipHairOrder",method = RequestMethod.GET)
    int plusVipHairOrder(@RequestParam String out_trade_no);

    @RequestMapping(value = "SelectAfterOrder",method = RequestMethod.GET)
    AfterOrder SelectAfterOrder(@RequestParam Long orderId);
}
