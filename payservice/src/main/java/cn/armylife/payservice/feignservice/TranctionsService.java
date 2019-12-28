package cn.armylife.payservice.feignservice;

import cn.armylife.common.domain.Transactions;
import cn.armylife.payservice.hystrix.TranctionsServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "paymentsservice",fallback= TranctionsServiceHystrix.class)
public interface TranctionsService {

    @RequestMapping(value = "Addtranctions",method = RequestMethod.GET)
    int Addtranctions(@RequestParam Long orderId,@RequestParam BigDecimal total,@RequestParam String wechatnum);


}
