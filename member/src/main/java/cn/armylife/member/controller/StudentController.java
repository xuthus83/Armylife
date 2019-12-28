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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
     * 通过appId查询商户信息
     * @param appid
     * @return
     */
    @RequestMapping("ShopIsDone")
    @ResponseBody
    public int ShopNewForArmylife(String appid){
        Member member=memberService.ShopNewForAppId(appid);
        if (member==null){
            return 0;
        }else if (member.getShopStatus()==1){
            return 3;
        }else if(member.getIsHot()==1){
            return 2;
        }
        return 1;
    }
    /**
     * 通过tagId查找商品
     * @param shopId
     * @return
     */
    @RequestMapping("productForTag")
    @ResponseBody
    public List<Product> productForTag(long shopId){
        return memberService.productForTag(shopId);
    };

    @RequestMapping("loginDev")
    @ResponseBody
    public int loginUser(String code, HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=memberService.loginDev(code);
        session.setAttribute("Student",memberService);
        return 1;

    }


}
