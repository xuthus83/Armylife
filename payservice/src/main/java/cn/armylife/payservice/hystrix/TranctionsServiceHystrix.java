package cn.armylife.payservice.hystrix;

import cn.armylife.payservice.feignservice.TranctionsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TranctionsServiceHystrix implements TranctionsService {



    @Value("${server.port}")
    int port;

    public int Addtranctions(Long orderId, BigDecimal total, String wechatnum){
        return port;
    };
}
