package cn.armylife.payservice.FeignService;

import cn.armylife.common.Domain.Transactions;
import cn.armylife.payservice.Hystrix.TranctionsServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "paymentsservice",fallback= TranctionsServiceHystrix.class)
public interface TranctionsService {

    @RequestMapping("Addtranctions")
    int Addtranctions(Transactions transactions);


}
