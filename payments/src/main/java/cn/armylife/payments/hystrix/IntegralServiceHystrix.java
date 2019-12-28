package cn.armylife.payments.hystrix;

import cn.armylife.payments.feignservice.IntegralService;
import org.springframework.stereotype.Component;

@Component
public class IntegralServiceHystrix implements IntegralService {

    @Override
    public int payintegralIncrease(Integer integral, Long memberId){
        return 0;
    }
}
