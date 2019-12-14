package cn.armylife.payments.FeignService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "integralservice" )
public interface integral {

    @RequestMapping(value = "integralIncrease",method = RequestMethod.GET)
    int integralIncrease(@RequestParam Integer integral);
}
