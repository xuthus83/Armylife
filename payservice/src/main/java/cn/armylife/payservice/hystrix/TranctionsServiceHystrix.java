package cn.armylife.payservice.hystrix;

import cn.armylife.common.domain.Transactions;
import cn.armylife.payservice.feignservice.TranctionsService;
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
