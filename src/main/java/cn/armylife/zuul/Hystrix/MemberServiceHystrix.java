package cn.armylife.zuul.Hystrix;

import cn.armylife.zuul.Feign.MemberService;

import javax.servlet.http.HttpServletRequest;

public class MemberServiceHystrix implements MemberService {

    @Override
    public int reloadSession(Integer memberId) {
        return 0;
    }
}
