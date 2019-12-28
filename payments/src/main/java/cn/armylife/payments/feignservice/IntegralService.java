package cn.armylife.payments.feignservice;

import cn.armylife.payments.hystrix.IntegralServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "integralservice",fallback = IntegralServiceHystrix.class )
public interface IntegralService {

    @RequestMapping(value = "payintegralIncrease",method = RequestMethod.GET)
    int payintegralIncrease(@RequestParam Integer integral,@RequestParam Long memberId);
}
