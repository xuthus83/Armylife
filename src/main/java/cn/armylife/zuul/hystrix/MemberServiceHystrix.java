package cn.armylife.zuul.hystrix;

import cn.armylife.zuul.feign.MemberService;

public class MemberServiceHystrix implements MemberService {

    @Override
    public int reloadSession(Integer memberId) {
        return 0;
    }
}
