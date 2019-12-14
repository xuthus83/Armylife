package cn.armylife.payservice.Hystrix;

import cn.armylife.common.Domain.Transactions;
import cn.armylife.payservice.FeignService.TranctionsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TranctionsServiceHystrix implements TranctionsService {



    @Value("${server.port}")
    int port;

    public int Addtranctions(Transactions transactions){
        return port;
    };
}
