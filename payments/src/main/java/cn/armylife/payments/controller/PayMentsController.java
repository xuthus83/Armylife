package cn.armylife.payments.controller;

import cn.armylife.common.config.AlipayConfig;
import cn.armylife.common.domain.*;
import cn.armylife.payments.domain.Alipay;
import cn.armylife.payments.feignservice.MarketService;
import cn.armylife.payments.feignservice.Payservice;
import cn.armylife.payments.feignservice.integral;
import cn.armylife.payments.service.PayMentsService;
import cn.armylife.payments.service.TransactionsService;
import cn.armylife.payments.utils.MemberWXMyConfigUtil;
import cn.armylife.payments.utils.NumberID;
import cn.armylife.payments.utils.WXPayUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class PayMentsController {

    @Autowired
    MemberWXMyConfigUtil wxMyConfigUtil;

    @Autowired
    Payservice payservice;

    @Autowired
    PayMentsService payMentsService;

    @Autowired
    MarketService marketService;

    @Autowired
    integral integral;
    @Autowired
    TransactionsService transactionsService;

    @Value("${server.port}")
    int port;

    Logger logger=Logger.getLogger("/PayMentsController.class");

    /**
     * 生成订单支付状态,向支付表插入支付记录
     * @param payments
     * @param request
     * @return
     */
    @RequestMapping("EnableWechatPayForOrder")
    @ResponseBody
    public Map<String, String> EnableWechatPayForOrder(Payments payments,Long ordersId,String attach, HttpServletRequest request){
        HttpSession session=request.getSession();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH/mm/ss");
        Date date=new Date();
        String creatTime=sdf.format(date);
        Member member=(Member)session.getAttribute("Student");
        ShopOrder shopOrder=marketService.selectOrder(ordersId);
        if (shopOrder.getDeliveryTotal()!=null){
            payments.setPayTotal(shopOrder.getOrderTotal().add(shopOrder.getDeliveryTotal()));
        }
        if(!attach.equals("3")){
            payments.setPayApp("1");
            payments.setPayTotal(shopOrder.getOrderTotal());
            payments.setPayName(shopOrder.getStuId());
            payments.setReceivName(shopOrder.getShopId());
            payments.setPayName(member.getMemberId());
            payments.setPayStatus("1");
            payments.setCreatTime(creatTime);
            payments.setPayDesc("订单支付");
        }else {
            AfterOrder afterOrder= marketService.SelectAfterOrder(ordersId);
            payments.setPayApp("1");
            payments.setPayTotal(afterOrder.getAfterOrderTotal());
            payments.setPayName(shopOrder.getStuId());
            payments.setReceivName(shopOrder.getShopId());
            payments.setPayStatus("1");
            payments.setCreatTime(creatTime);
            payments.setPayDesc("订单支付");
        }
        Long id= NumberID.nextId(port);
        payments.setPaymentsId(id);
        String desc=payments.getPayDesc();
        String total=String.valueOf(payments.getPayTotal());
        payMentsService.insert(payments);
        Map<String, String> result=payservice.order(desc,total,String.valueOf(id),attach);//返回调起支付所需数据
        return result;
    }

    @RequestMapping("ShopWechatExtract")
    @ResponseBody
    public int ShopWechatExtract(HttpServletRequest request,String total){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        if (payservice.WechatExtract(member,total)==1){
            return 1;
        };
        return 0;
    }

    @RequestMapping("DeliveryWechatExtract")
    @ResponseBody
    public int DeliveryWechatExtract(HttpServletRequest request,String total){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Delivery");
        if (payservice.WechatExtract(member,total)==1){
            return 1;
        };
        return 0;
    }

    @RequestMapping("/paycallback")
    @ResponseBody
    public Map<String, String> paycallback(HttpServletRequest request, HttpServletResponse response)throws Exception{
        WXPay wxPay=null;
        InputStream inputStream = null;
        Map<String, String> notifyMap = new HashMap<String, String>();
        try {
            wxPay=new WXPay(wxMyConfigUtil);
            inputStream = request.getInputStream();
            String xml = WXPayUtil.inputStream2String(inputStream);
//            String xml="123";
            notifyMap = WXPayUtil.xmlToMap(xml);//将微信发的xml转map
            //logger.info("支付系统返回支付结果"+xml);

            if (wxPay.isPayResultNotifySignatureValid(notifyMap)){
                if(notifyMap.get("return_code").equals("SUCCESS")){
                    logger.info("return_code是："+notifyMap.get("return_code"));
                    // 交易成功
                    if(notifyMap.get("result_code").equals("SUCCESS")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH/mm/ss");
                        Date date=new Date();
                        String creatTime=sdf.format(date);
                        String transaction_id=notifyMap.get("transaction_id");
                        String attach=notifyMap.get("attach");
                        String out_trade_no=notifyMap.get("out_trade_no");
                        String total_fee=notifyMap.get("total_fee");
                        if (attach.equals("2")){
                            marketService.plusVipHairOrder(out_trade_no);
                        }
                        else if(attach.equals("3")){
                            AfterOrder afterOrder=new AfterOrder();
                            afterOrder.setAfterOrderId(Long.valueOf(out_trade_no));
                            afterOrder.setIsPay(1);
                            payMentsService.updateafterOrderForPay(afterOrder);
                        }
                        if (!attach.equals("0")){
                            integral.integralIncrease(Integer.valueOf(total_fee));
                        }
                        Payments payments=new Payments();
                        payments.setPayNumber(transaction_id);
                        payments.setPayStatus("2");
                        payments.setEndTime(creatTime);
                        payments.setPayApp("Wechat");
                        payments.setPaymentsId(Long.valueOf(out_trade_no));
                        payMentsService.updatePayMentsForCallback(payments);
                    }
                }else{
                    // 交易失败的处理
                    notifyMap.put("0","0");
                    return notifyMap;
                }
            }else {
                logger.info("签名失败"+notifyMap);

            }

            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>"); //告知微信支付系统已收到消息
            inputStream.close();
        } catch (Exception e) {
            // 异常的处理
        }
        return notifyMap;
    }

    @RequestMapping("AlipayCallback")
    @ResponseBody
    public Map<String, String> AlipayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        response.setContentType("text/html;charset=utf-8");
        response.reset();
        //获取支付宝POST过来反馈信息
        response.getOutputStream();

        Map<String,String> params=new HashMap<>();
        Map requestParams =request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        String trade_no = new String();
        String total_amount = new String();
        String trade_status = new String();
        String out_trade_no = new String();
        String passback_params=new String();
        try{
        //商户订单号
        out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("商户订单号"+out_trade_no);
        //支付宝交易号

        trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info(  "交易号"+trade_no);
        total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
        logger.info(  "支付宝订单资金"+total_amount);
        //交易状态
        trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("支付状态:"+trade_status);

        passback_params=new String(request.getParameter("passback_params").getBytes("ISO-8859-1"),"UTF-8");
    }catch (NullPointerException e){
          logger.info("空指针异常:"+e);
        }
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Payments payments=new Payments();//创建支付订单数据
        payments.setPayNumber(trade_no);
        payments.setPaymentsId(Long.valueOf(out_trade_no));
        payments.setPayStatus("2");
        payments.setPayApp("2");
        if (!trade_status.equals("TRADE_SUCCESS")){
            return null;
        }
        logger.info("通过");
        payMentsService.updatePayMentsForCallback(payments);
        if (passback_params.equals("2")){
            marketService.plusVipHairOrder(out_trade_no);
        }
        else if (passback_params.equals("3")){
            AfterOrder afterOrder=new AfterOrder();
            afterOrder.setAfterOrderId(Long.valueOf(out_trade_no));
            afterOrder.setIsPay(1);
            payMentsService.updateafterOrderForPay(afterOrder);
        }
        if (!passback_params.equals("0")){
            integral.integralIncrease(Integer.valueOf(total_amount));
        }
        if (verify_result){
            response.getOutputStream().println("success");	//请不要修改或删除
            response.getOutputStream().close();
        }else {
            response.getOutputStream().println("fail");
            return params;
        }
        return params;
    }

    @RequestMapping("Addtranctions")
    @ResponseBody
    public int Addtranctions(Transactions transactions){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date=new Date();
        String endtime=sdf.format(date);
        transactions.setEndTime(endtime);
        return transactionsService.insert(transactions);
    }

    @RequestMapping("AlipayRefundCallback")
    @ResponseBody
    public Map<String, String> AlipayRefundCallback(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        response.setContentType("text/html;charset=utf-8");
        response.reset();
        //获取支付宝POST过来反馈信息
        response.getOutputStream();

        Map<String,String> params=new HashMap<>();
        Map requestParams =request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        String trade_no = new String();
        String refund_fee = new String();
        String fund_change = new String();
        String out_trade_no = new String();
        String buyer_logon_id=new String();
        try{
            //商户订单号
            out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            logger.info("商户订单号"+out_trade_no);
            //支付宝交易号
            trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            logger.info(  "交易号"+trade_no);
            //支付宝退款资金
            refund_fee = new String(request.getParameter("refund_fee").getBytes("ISO-8859-1"),"UTF-8");
            logger.info(  "支付宝退款资金"+refund_fee);
            //交易状态
            fund_change = new String(request.getParameter("fund_change").getBytes("ISO-8859-1"),"UTF-8");
            //用户的登录id
            buyer_logon_id=new String(request.getParameter("buyer_logon_id ").getBytes("ISO-8859-1"),"UTF-8");
            logger.info("登录人Id:"+fund_change);

        }catch (NullPointerException e){
            logger.info("空指针异常:"+e);
        }
        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Payments payments=new Payments();//创建支付订单数据
        BigDecimal refund=new BigDecimal(refund_fee);
        payments.setRefundsNumber(trade_no);
        payments.setPaymentsId(Long.valueOf(out_trade_no));
        payments.setRefundTotal(refund);
        payments.setRefundDesc(buyer_logon_id);
        payments.setPayRefund(1);
        if (fund_change.equals("Y")){
            logger.info("通过");
            payMentsService.updatePayMentsForCallback(payments);
        }
        if (verify_result){
            response.getOutputStream().println("success");	//请不要修改或删除
            response.getOutputStream().close();
        }else {
            response.getOutputStream().println("fail");
            return params;
        }
        return params;
    }

    @RequestMapping("AlipayTransfer")
    @ResponseBody
    public int transfer(String total,String number,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        BigDecimal amount= new BigDecimal(total);
        return payservice.transfer(amount,number,"商家收入");
    }

    @RequestMapping("WechatPayTransfer")
    @ResponseBody
    public int WechatPayTransfer(Long orderCardId, BigDecimal refundfree,BigDecimal totalfree,String desc,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        Double total=totalfree.doubleValue();//订单金额
        Double refund=refundfree.doubleValue();//退款金额
        return payservice.orderrefund(orderCardId,refund,total,desc);
    }

    /**
     * 商品
     * @param ordersId
     * @param request
     * @return
     */
    @RequestMapping("AlipayPay")
    @ResponseBody
    public String AlipayPay(Long ordersId,HttpServletRequest request,String passback_params){
        HttpSession session=request.getSession();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH/mm/ss");
        Date date=new Date();
        String creatTime=sdf.format(date);
        Payments payments=new Payments();
        Member member=(Member)session.getAttribute("Student");
        ShopOrder shopOrder=marketService.selectOrder(ordersId);
        logger.info(shopOrder.toString());
        if (passback_params==null){
            passback_params="1";
        }
        if (shopOrder.getDeliveryTotal()!=null){
            payments.setPayTotal(shopOrder.getOrderTotal().add(shopOrder.getDeliveryTotal()));
        }
        if (!passback_params.equals("3")){
            payments.setPayApp("2");
            payments.setPayTotal(shopOrder.getOrderTotal());
            payments.setPayName(shopOrder.getStuId());
            payments.setReceivName(shopOrder.getShopId());
            payments.setPayName(member.getMemberId());
            payments.setPayStatus("1");
            payments.setCreatTime(creatTime);
            payments.setPayDesc("订单支付");
            payments.setOrderId(ordersId);
        }
        else {
            AfterOrder afterOrder= marketService.SelectAfterOrder(ordersId);
            payments.setPayApp("2");
            payments.setPayTotal(afterOrder.getAfterOrderTotal());
            payments.setPayName(shopOrder.getStuId());
            payments.setReceivName(shopOrder.getShopId());
            payments.setPayStatus("1");
            payments.setCreatTime(creatTime);
            payments.setPayDesc("订单支付");
            payments.setOrderId(ordersId);
        }
        Long id= NumberID.nextId(port);
        payments.setPaymentsId(id);
        String total=String.valueOf(payments.getPayTotal());
        payMentsService.insert(payments);
        Alipay alipay=payservice.alipay(String.valueOf(id),"订单支付",total,"订单支付宝支付",passback_params);
        return alipay.getNum();
    }

    /**
     * 通过订单Id查询支付记录
     * @param orderId
     * @return
     */
    @RequestMapping("selectPaments")
    @ResponseBody
    public Payments selectPaments(Long orderId){
        return payMentsService.selectPaments(orderId);
    };

    @RequestMapping
    @ResponseBody
    public int JoinBarberMember(HttpServletRequest request,Long orderId){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        ShopOrder shopOrder=marketService.selectOrder(orderId);
        Payments payments=new Payments();
        payments.setOrderId(orderId);
        payments.setPayDesc("理发Vip");
        BigDecimal total=new BigDecimal(100);
        payments.setPayTotal(total);
        payments.setPayName(member.getMemberId());
        payments.setReceivName(shopOrder.getShopId());
        return 1;
    }
}
