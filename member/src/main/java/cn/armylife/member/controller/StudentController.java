package cn.armylife.member.controller;

import cn.armylife.common.domain.Member;
import cn.armylife.common.domain.Product;
import cn.armylife.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StudentController {
    Logger logger= LoggerFactory.getLogger(StudentController.class);

    @Autowired
    MemberService memberService;

    /**
     * 通过appId查询商户信息
     * @param appid
     * @return
     */
    @RequestMapping("ShopNewForAppId")
    @ResponseBody
    public Member ShopNewForAppId(String appid){
        return memberService.ShopNewForAppId(appid);
    }
    /**
     * 通过tagId查找商品
     * @param tagId
     * @return
     */
    @RequestMapping("productForTag")
    @ResponseBody
    public List<Product> productForTag(long shopId){
        return memberService.productForTag(shopId);
    };
}
