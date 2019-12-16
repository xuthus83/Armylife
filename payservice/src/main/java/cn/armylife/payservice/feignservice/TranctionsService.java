package cn.armylife.payservice.feignservice;

import cn.armylife.common.domain.Transactions;
import cn.armylife.payservice.hystrix.TranctionsServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "paymentsservice",fallback= TranctionsServiceHystrix.class)
public interface TranctionsService {

    @RequestMapping("Addtranctions")
    int Addtranctions(Transactions transactions);


}
