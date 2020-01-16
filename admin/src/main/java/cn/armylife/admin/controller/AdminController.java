package cn.armylife.admin.controller;

import cn.armylife.admin.domain.Admin;
import cn.armylife.admin.domain.Member;
import cn.armylife.admin.domain.ShopOrder;
import cn.armylife.admin.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author :xuthus
 */
@Controller
public class AdminController {

    Logger logger=Logger.getLogger("OrderController.class");

    @Autowired
    AdminService adminService;

    /**
     * 查询用户总数与当日收入
     * @return
     */
    @RequestMapping("indexUsers")
    @ResponseBody
    public Map<String,Integer> numForUsers(){
        Integer num=adminService.numberForUsers();
        logger.info("num:"+num);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Integer msg= adminService.incomeToDay(sdf.format(new Date()));
        if (msg==null){
            msg=0;
        }
        logger.info("msg:"+msg);
        Map<String,Integer> map=new HashMap<>();
        map.put("num",num);
        map.put("total",msg);
        return map;
    }

    /**
     * 查询时间段人数
     * @param time
     * @return
     */
    @RequestMapping("weekNumForUsers")
    @ResponseBody
    public Integer weekNumForUsers(String time){
        return adminService.weekNumForUsers(time);
    };

    /**
     * 全部用户
     * @return
     */
    @RequestMapping("userSelectAll")
    @ResponseBody
    public PageInfo<Member> userSelectAll(int pageNum){
        PageHelper.startPage(pageNum,10);
        PageInfo<Member> pageInfo=new PageInfo<>(adminService.userSelectAll());
        return pageInfo;
    }

    /**
     * 全部订单
     * @return
     */
    @RequestMapping("orderSelectAll")
    @ResponseBody
    public PageInfo<ShopOrder> orderSelectAll(int pageNum){
        PageHelper.startPage(pageNum,10);
        PageInfo<ShopOrder> pageInfo=new PageInfo<>(adminService.orderSelectAll());
        return pageInfo;
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping("loginAdmin")
    @ResponseBody
    public int loginAdmin(Admin admin, HttpServletRequest request){
        HttpSession session=request.getSession();
        Admin admin1=adminService.loginAdmin(admin);
        session.setAttribute("admin",admin1);
        if (admin1==null){
            return 0;
        }
        return 1;
    };

    /**
     * 登录信息
     * @return
     */
    @RequestMapping("testlogin")
    @ResponseBody
    public int testLogin(HttpServletRequest request){
        HttpSession session=request.getSession();
        Admin admin1=(Admin) session.getAttribute("admin");
        if (admin1==null){
            return 0;
        }
        return 1;
    };

    /**
     * 登录信息
     * @return
     */
    @RequestMapping("logon")
    @ResponseBody
    public int logon(HttpServletRequest request){
        HttpSession session=request.getSession();
        Admin admin1=(Admin) session.getAttribute("admin");
        if (admin1==null){
            return 0;
        }
        return 1;
    };
}

