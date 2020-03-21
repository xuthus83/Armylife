package cn.armylife.payments.controller;

import cn.armylife.common.domain.Member;
import cn.armylife.payments.domain.RedEnvelopes;
import cn.armylife.payments.service.RedEnvelopesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * creat by xuthus on 2020-03-03
 */
@Slf4j
@Controller
public class RedEnvelopesController {

    @Autowired
    RedEnvelopesService redEnvelopesService;

    /**
     * 创建红包
     * @param redEnvelopes
     * @return
     */
    @RequestMapping("redInsert")
    @ResponseBody
    public int insert(RedEnvelopes redEnvelopes,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=new Member();//(Member)session.getAttribute("Students");
        member.setMemberId(11L);
        redEnvelopes.setUserId(member.getMemberId());
        return redEnvelopesService.insert(redEnvelopes);
    };

    /**
     * 更新用户红包信息
     * @param redEnvelopes
     * @return
     */
    @RequestMapping("redUpdate")
    @ResponseBody
    public int update(RedEnvelopes redEnvelopes, HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member) session.getAttribute("Students");
        redEnvelopes.setUserId(member.getMemberId());
        SimpleDateFormat simpleFormatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date=new Date();
        String time=simpleFormatter.format(date);
        redEnvelopes.setCreatTime(time);
        return redEnvelopesService.update(redEnvelopes);
    };

    /**
     *  根据红包Id查询红包
     * @param redId
     * @return
     */
    @RequestMapping("selectRedForId")
    @ResponseBody
    public RedEnvelopes selectRedForId(Long redId){
        return redEnvelopesService.selectRedForId(redId);
    };

    /**
     *  根据用户查询红包
     * @param request
     * @return
     */
    @RequestMapping("selectRedForUser")
    @ResponseBody
    public List<RedEnvelopes> selectRedForUser(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=null;//(Member)session.getAttribute("Students");
        return redEnvelopesService.selectRedForUser(11L);
    };

    /**
     * 将红包状态改为1(使用红包)
     * @param redId
     * @return
     */
    @ResponseBody
    @RequestMapping("updateRedStatus")
    public int updateRedStatus(Long redId){
        return redEnvelopesService.updateRedStatus(redId);
    };
}
