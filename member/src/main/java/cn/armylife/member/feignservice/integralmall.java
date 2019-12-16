package cn.armylife.member.feignservice;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "integralservice")
public interface integralmall {
}
