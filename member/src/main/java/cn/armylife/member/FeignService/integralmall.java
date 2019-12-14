package cn.armylife.member.FeignService;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "integralservice")
public interface integralmall {
}
