package cn.armylife.integralmall.controller;

import cn.armylife.common.domain.Alipay;
import cn.armylife.common.domain.Member;
import cn.armylife.common.domain.Payments;
import cn.armylife.common.domain.PointsExchangeRecord;
import cn.armylife.common.util.NumberID;
import cn.armylife.integralmall.service.IntegralMallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class payController {
    Logger logger= LoggerFactory.getLogger(payController.class);

    @Value("${server.port}")
    int port;

    @Autowired
    IntegralMallService integralMallService;
    @Autowired
    cn.armylife.integralmall.feign.payservice payservice;

    @RequestMapping("integralAliPay")
    @ResponseBody
    public String integralAliPay(HttpServletRequest request,Long pointsExchangeRecordId){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        String WIDout_trade_no=null;
        String WIDsubject=null;
        String WIDtotal_amount=null;
        String WIDbody=null;
        String passback_params=null;
        Long int1= NumberID.nextId(port);
        PointsExchangeRecord exchangeRecord=integralMallService.findExchangeRecordForId(pointsExchangeRecordId);
        Payments payments=new Payments();
        payments.setPayName(member.getMemberId());
        payments.setPayApp("1");
        payments.setPayTotal(exchangeRecord.getUsageAmount());
        payments.setOrderId(exchangeRecord.getPointsExchangeRecordId());
        payments.setPaymentsId(int1);
        WIDout_trade_no=String.valueOf(int1);
        WIDtotal_amount=String.valueOf(payments.getPayTotal());
        WIDbody="积分兑换支付金额";
        WIDsubject=exchangeRecord.getItemsExchanged();
        passback_params="0";
        integralMallService.paymentsinsert(payments);
        Alipay alipay=payservice.alipay(WIDout_trade_no,WIDsubject,WIDtotal_amount,WIDbody,passback_params);
        return alipay.getNum();
    };

    @RequestMapping("integralWechatPay")
    @ResponseBody
    public Map<String,String> integralWechatPay(HttpServletRequest request,Long pointsExchangeRecordId){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        String orderDesc=null;
        String Total=null;
        String orderCardID=null;
        String attach=null;
        Long int1= NumberID.nextId(port);
        PointsExchangeRecord exchangeRecord=integralMallService.findExchangeRecordForId(pointsExchangeRecordId);
        Payments payments=new Payments();
        payments.setPayName(member.getMemberId());
        payments.setPayApp("1");
        payments.setPayTotal(exchangeRecord.getUsageAmount());
        payments.setOrderId(exchangeRecord.getPointsExchangeRecordId());
        payments.setPaymentsId(int1);
        orderCardID=String.valueOf(int1);
        Total=String.valueOf(payments.getPayTotal());
        orderDesc="积分兑换支付金额";
        attach="0";
        integralMallService.paymentsinsert(payments);
        Map<String,String> map=payservice.order(orderDesc,Total,orderCardID,attach);
        return map;
    };
}
