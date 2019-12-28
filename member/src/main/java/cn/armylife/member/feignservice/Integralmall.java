package cn.armylife.member.feignservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "integralservice")
public interface Integralmall {

    @RequestMapping(value = "registerIntegral",method = RequestMethod.GET)
    int registerIntegral(@RequestParam Long memberId);
}
