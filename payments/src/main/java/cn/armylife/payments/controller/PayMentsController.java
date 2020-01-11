package cn.armylife.payments.controller;

import cn.armylife.common.config.AlipayConfig;
import cn.armylife.common.domain.*;
import cn.armylife.payments.domain.Alipay;
import cn.armylife.payments.feignservice.IntegralService;
import cn.armylife.payments.feignservice.MarketService;
import cn.armylife.payments.feignservice.MemberService;
import cn.armylife.payments.feignservice.Payservice;
import cn.armylife.payments.service.PayMentsService;
import cn.armylife.payments.service.TransactionsService;
import cn.armylife.payments.utils.MemberWXMyConfigUtil;
import cn.armylife.payments.utils.MessageWechat;
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
    TransactionsService transactionsService;

    @Autowired
    IntegralService integralService;

    @Autowired
    MemberService memberService;

    @Autowired
    MessageWechat messageWechat;

    @Value("${server.port}")
    int port;

    Logger logger=Logger.getLogger("/PayMentsController.class");

    /**
     * 生成订单支付状态,向支付表插入支付记录
     * @param request
     * @return
     */
    @RequestMapping("EnableWechatPayForOrder")//订单支付
    @ResponseBody
    public Map<String, String> EnableWechatPayForOrder(Payments payments,Long ordersId,String attach, HttpServletRequest request){
        HttpSession session=request.getSession();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        Date date=new Date();
        String creatTime=sdf.format(date);
        Member member=(Member)session.getAttribute("Student");
        logger.info("执行获取订单支付订单信息");
        ShopOrder shopOrder=payMentsService.selectOrder(ordersId);
        logger.info("订单id"+ordersId);
        logger.info("订单信息:"+shopOrder.toString());
        Payments payments1=payMentsService.selectPaments(ordersId);
        if (payments1!=null){
            int msg=Integer.valueOf(payments1.getPayStatus());
            switch (msg){
                case 1:
                    String id=String.valueOf(payments1.getPaymentsId());
                    payments.setPayApp("1");
                    payments.setPaymentsId(payments1.getPaymentsId());
                    String desc=payments1.getPayDesc();
                    String total=String.valueOf(payments1.getPayTotal());
                    payMentsService.updatePayMentsForCallback(payments);
                    Map<String, String> result=payservice.order(desc,total,id,attach,member.getMemberWechat(),request);//返回调起支付所需数据
                    return result;
                case 2:
                    logger.info("已支付");
                    return null;
                case 3:
                    logger.info("已退款");
                    return null;
            }

        }
        if (attach==null){
            attach="0";
        }
        if(!attach.equals("3")){
            try{
                if (shopOrder.getDeliveryTotal()!=null){
                    payments.setPayTotal(shopOrder.getOrderTotal().add(shopOrder.getDeliveryTotal()));
                }else {
                    payments.setPayTotal(shopOrder.getOrderTotal());
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            payments.setPayName(shopOrder.getStuId());
            payments.setReceivName(shopOrder.getShopId());
            payments.setPayName(member.getMemberId());
            payments.setPayStatus("1");
            payments.setCreatTime(creatTime);
            payments.setPayDesc("订单支付");
        }
        Long id= NumberID.nextId(port);
        payments.setPaymentsId(id);
        payments.setOrderId(ordersId);
        payments.setPayApp("1");
        String desc=payments.getPayDesc();
        String total=String.valueOf(payments.getPayTotal());
        logger.info("支付金额:"+total);
        payMentsService.insert(payments);
        Map<String, String> result=payservice.order(desc,total,String.valueOf(id),attach,member.getMemberWechat(),request);//返回调起支付所需数据
        return result;
    }

    @RequestMapping("ShopWechatExtract")//商家微信提现
    @ResponseBody
    public int ShopWechatExtract(HttpServletRequest request,String total){
        HttpSession session=request.getSession();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        Date date=new Date();
        String creatTime=sdf.format(date);
        Member olduser=(Member)session.getAttribute("Shop");
        Member member=payMentsService.selectMember(olduser.getMemberId());
        String openid=member.getMemberWechat();
        BigDecimal reward=member.getMemberTotal();
        Long memberId=member.getMemberId();
        logger.info("余额"+member.getMemberTotal());
        BigDecimal memberTotal=member.getMemberTotal();
        BigDecimal popTotal=new BigDecimal(total);
        Double amount=popTotal.doubleValue();
        total=String.valueOf(amount*0.99);
        if (payservice.WechatExtract(openid,reward,memberId,total)==1){
            Member user=new Member();
            user.setMemberTotal(memberTotal.subtract(popTotal));
            user.setMemberId(memberId);
            String endtime=sdf.format(date);
            Transactions transactions=new Transactions();
            transactions.setPayTotal(BigDecimal.valueOf(amount));
            transactions.setPayDesc("提现");
            transactions.setPayUser(memberId);
            transactions.setCreatTime(creatTime);
            transactions.setEndTime(endtime);
            transactionsService.insert(transactions);
            return payMentsService.updateShop(user);
        };
        return 0;
    }

    @RequestMapping("DeliveryWechatExtract")//跑腿微信提现
    @ResponseBody
    public int DeliveryWechatExtract(HttpServletRequest request,String total){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        Date date=new Date();
        String creatTime=sdf.format(date);
        HttpSession session=request.getSession();
        Member olduser=(Member)session.getAttribute("Delivery");
        Member member=payMentsService.selectMember(olduser.getMemberId());
        String openid=member.getMemberWechat();
        BigDecimal reward=member.getMemberTotal();
        Long memberId=member.getMemberId();
        if (payservice.WechatExtract(openid,reward,memberId,total)==1){
            Member user=new Member();
            user.setMemberTotal(member.getMemberTotal().subtract(new BigDecimal(total)));
            user.setMemberId(memberId);
            String endtime=sdf.format(date);
            Transactions transactions=new Transactions();
            transactions.setPayTotal(new BigDecimal(total));
            transactions.setPayDesc("提现");
            transactions.setPayUser(memberId);
            transactions.setCreatTime(creatTime);
            transactions.setEndTime(endtime);
            transactionsService.insert(transactions);
            return payMentsService.updateShop(user);
        };
        return 0;
    }

    @RequestMapping("/paycallback")//微信支付回调通知
    @ResponseBody
    public Map<String, String> paycallback(HttpServletRequest request, HttpServletResponse response)throws Exception{
        logger.info("微信支付收到异步通知");
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
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
                        Date date=new Date();
                        String creatTime=sdf.format(date);
                        String transaction_id=notifyMap.get("transaction_id");
                        logger.info("微信订单号:"+transaction_id);
                        String attach=notifyMap.get("attach");
                        logger.info("attach:"+attach);
                        String out_trade_no=notifyMap.get("out_trade_no");
                        String openid=notifyMap.get("openid");
                        logger.info("商户订单:"+out_trade_no);
                        String total_fee=notifyMap.get("total_fee");
                        logger.info("支付金额:"+total_fee);
                        Member user=payMentsService.selectMemberForOpenid(openid);//获取支付者微信openId
                        //创建并根据不同状态进行处理
                        ShopOrder shopOrder=new ShopOrder();
                        shopOrder.setOrdersStatus("1");
                        Payments payments=new Payments();
                        payments.setPayNumber(transaction_id);
                        payments.setPayStatus("2");
                        Double total=Integer.valueOf(total_fee)*0.01;
                        logger.info("积分:"+total.intValue());
                        integralService.payintegralIncrease(total.intValue(),user.getMemberId());
                        Long paymentsId=Long.valueOf(out_trade_no);
                        Payments payment1=payMentsService.selectOrderForPayments(paymentsId);
                        Long ordersId=payment1.getOrderId();
                        shopOrder.setOrdersId(payment1.getOrderId());
                        ShopOrder order=payMentsService.selectOrder(ordersId);
                        if (order.getIsexpress()==1){
                            shopOrder.setOrdersStatus("3");
                        }else if (attach.equals("2")){
                            logger.info("进入理发店流程");
                            shopOrder.setOrdersStatus("3");
                        }else if (attach.equals("4")){
                            shopOrder.setOrdersStatus("6");
                            marketService.updateHairAmount(String.valueOf(total),payment1.getPayName(),paymentsId);
                        }else if(attach.equals("6")){
                            int num=total.intValue();
                            int people=num/2;
                            marketService.plusOrderPeoPle(people,ordersId);
                        }else if(attach.equals("3")){
//                            shopOrder.setOrdersStatus("3");
                            AfterOrder afterOrder=new AfterOrder();
                            afterOrder.setAfterOrderId(Long.valueOf(out_trade_no));
                            afterOrder.setIsPay(1);
                            payMentsService.updateafterOrderForPay(afterOrder);
                        }
                        int ordernews=payMentsService.updateShopOrder(shopOrder);
                        if (ordernews==0){
                            logger.info("出错"+ordernews);
                        }
                        payments.setEndTime(creatTime);
                        payments.setPayApp("1");
                        payments.setPaymentsId(Long.valueOf(out_trade_no));
                        int paymentsnews=payMentsService.updatePayMentsForCallback(payments);
                        if (paymentsnews==0){
                            logger.info("出错"+ordernews);
                        }
                        WXtemplate wXtemplate=new WXtemplate();
                        wXtemplate.setTemplate("WD4fbaWwjhJzwB1VXV3jFWqNYpSvD_Dye1sUJ5xZCus");
                        wXtemplate.setOpenid(openid);
                        wXtemplate.setFirst("您好,已成功创建订单!");
                        wXtemplate.setRemark1("点击可查看订单详情");
                        Map<String,String> key=new HashMap<>();
                        key.put("key1",String.valueOf(ordersId));
                        key.put("key2",creatTime);
                        wXtemplate.setKey(key);
                        wXtemplate.setUrl("Students/OrderDetails3.html?ordersId="+ordersId);
                        messageWechat.newOrderService(wXtemplate);
                        WXtemplate wXtemplate1=new WXtemplate();
                        if (order.getIsexpress()!=1) {
                            Member shop = payMentsService.selectMember(payment1.getReceivName());
                            wXtemplate1.setTemplate("_9hSju78I4FRSgSShcMN08-e5zIUGdCp87YvXOBiMTo");
                            wXtemplate1.setOpenid(shop.getMemberWechat());
                            wXtemplate1.setFirst("您好,已有新订单!");
                            wXtemplate1.setRemark1("点击可查看订单详情");
                            Map<String, String> key1 = new HashMap<>();
                            key1.put("key1", "查看详情");
                            key1.put("key2", creatTime);
                            key1.put("key3", "理发店");
                            key1.put("key4", shopOrder.getMemberName());
                            key1.put("key5", "已付款");
                            wXtemplate1.setKey(key1);
                            wXtemplate1.setUrl("Business/OrderDetails3.html?ordersId=" + ordersId);
                            messageWechat.newOrderService(wXtemplate1);
                        }
                    }
                }else{
                    // 交易失败的处理
                    notifyMap.put("0","0");
                    return notifyMap;
                }
            }else {
                logger.info("签名失败"+notifyMap);

            }
            response.getOutputStream().print("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>"); //告知微信支付系统已收到消息
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifyMap;
    }

    @RequestMapping("/Redundcallback")
    @ResponseBody
    public Map<String, String> payCallBack(HttpServletRequest request, HttpServletResponse response)throws Exception {
        logger.info("微信支付退款收到异步通知");
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
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
                        Date date=new Date();
                        String creatTime=sdf.format(date);
                        String transaction_id=notifyMap.get("transaction_id");
                        logger.info("微信订单号:"+transaction_id);
                        String attach=notifyMap.get("attach");
                        logger.info("attach:"+attach);
                        String out_trade_no=notifyMap.get("out_trade_no");
//                        String openid=notifyMap.get("openid");
                        logger.info("商户订单:"+out_trade_no);
                        String total_fee=notifyMap.get("total_fee");
                        logger.info("支付金额:"+total_fee);
                        Double total=Integer.valueOf(total_fee)*0.01;
                        Payments payments=new Payments();
                        payments.setRefundsNumber(transaction_id);
                        payments.setPayStatus("3");
                        payments.setEndTime(creatTime);
                        payments.setRefunsTime(creatTime);
                        payments.setRefundDesc("订单退款");
                        payments.setRefundTotal(new BigDecimal(total));
                        payments.setPayRefund(1);
                        payments.setPaymentsId(Long.valueOf(out_trade_no));
                        Member shop=new Member();
                        BigDecimal memberTotal=shop.getMemberTotal();
                        BigDecimal popTotal=new BigDecimal(total);
                        BigDecimal newTotal=memberTotal.subtract(popTotal);
                        Member member=payMentsService.selectMember(payments.getReceivName());
                        shop.setMemberTotal(newTotal);
                        shop.setMemberId(member.getMemberId());
                        payMentsService.updateShop(shop);
                        int paymentsnews=payMentsService.updatePayMentsForCallback(payments);
                        if (paymentsnews==0){
                            logger.info("出错"+paymentsnews);
                        }
                    }
                }else{
                    // 交易失败的处理
                    notifyMap.put("0","0");
                    return notifyMap;
                }
            }else {
                logger.info("签名失败"+notifyMap);

            }
            response.getOutputStream().print("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>"); //告知微信支付系统已收到消息
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notifyMap;
    }

    @RequestMapping("AlipayCallback")//支付宝支付回调通知
    @ResponseBody
    public Map<String, String> AlipayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        logger.info("支付宝支付收到异步通知");
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
        boolean verify_result = AlipaySignature.rsaCertCheckV1(params, AlipayConfig.alipay_cert_path, AlipayConfig.CHARSET, "RSA2");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Payments payments=new Payments();//创建支付订单数据
        payments.setPayNumber(trade_no);
        payments.setPaymentsId(Long.valueOf(out_trade_no));
        payments.setPayStatus("2");
        payments.setPayApp("2");
        payMentsService.updatePayMentsForCallback(payments);

        Long paymentsId=Long.valueOf(out_trade_no);
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setOrdersStatus("1");
        Payments payment1=payMentsService.selectOrderForPayments(paymentsId);
        shopOrder.setOrdersId(payment1.getOrderId());
        payMentsService.updateShopOrder(shopOrder);
        if (!trade_status.equals("TRADE_SUCCESS")){
            return null;
        }
        Member user=payMentsService.selectMember(payment1.getPayName());
        logger.info("通过");
        payMentsService.updatePayMentsForCallback(payments);
        if (passback_params.equals("2")){
            shopOrder.setOrdersStatus("6");
            marketService.plusVipHairOrder(out_trade_no,user.getMemberId());
        }
        else if (passback_params.equals("3")){
            AfterOrder afterOrder=new AfterOrder();
            afterOrder.setAfterOrderId(Long.valueOf(out_trade_no));
            afterOrder.setIsPay(1);
            payMentsService.updateafterOrderForPay(afterOrder);
        }
        integralService.payintegralIncrease(Integer.valueOf(total_amount),user.getMemberId());
        if (verify_result){
            response.getOutputStream().println("success");	//请不要修改或删除
            response.getOutputStream().close();
        }else {
            response.getOutputStream().println("fail");
            return params;
        }
        return params;
    }

    @RequestMapping("Addtranctions")//添加记录
    @ResponseBody
    public int Addtranctions(HttpServletRequest request,Long orderId,BigDecimal total,String wechatnum){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date=new Date();
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        String endtime=sdf.format(date);
        Transactions transactions=new Transactions();
        transactions.setPayUser(member.getMemberId());
        transactions.setTranactionsId(orderId);
        String creartime=sdf.format(date);
        transactions.setPayTotal(total);
        transactions.setPayDesc("提现");
        transactions.setCreatTime(creartime);
        transactions.setPayWechatnum(wechatnum);
        transactions.setEndTime(endtime);
        return transactionsService.insert(transactions);
    }

    @RequestMapping("AlipayRefundCallback")//支付宝退款通知
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
        boolean verify_result = AlipaySignature.rsaCertCheckV1(params, AlipayConfig.alipay_cert_path, AlipayConfig.CHARSET, "RSA2");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Payments payments=new Payments();//创建支付订单数据
        BigDecimal refund=new BigDecimal(refund_fee);
        payments.setRefundsNumber(trade_no);
        payments.setPaymentsId(Long.valueOf(out_trade_no));
        payments.setRefundTotal(refund);
        payments.setRefundDesc(buyer_logon_id);
        payments.setPayRefund(1);
        if (fund_change.equals("Y")){
//            logger.info("通过");
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

    /**
     * 支付宝退款
     * @param WIDout_trade_no
     * @param WIDsubject
     * @param WIDrefund_amount
     * @param WIDbody
     * @param request
     * @return
     */
    @RequestMapping("Alipayrefund")//支付宝退款
    @ResponseBody
    public int Alipayrefund(String WIDout_trade_no, String WIDsubject, String WIDrefund_amount, String WIDbody,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        Payments p=payMentsService.selectPaments(Long.valueOf(WIDout_trade_no));
//        logger.info("支付订单Id"+p.getPaymentsId());
        return payservice.Alipayrefund(String.valueOf(p.getPaymentsId()),WIDsubject,WIDrefund_amount,WIDbody);
    }

    /**
     * 微信订单退款
     * @param orderCardId
     * @param refundfree
     * @param totalfree
     * @param desc
     * @param request
     * @return
     */
    @RequestMapping("WechatPayTransfer")//微信订单退款
    @ResponseBody
    public int WechatPayTransfer(Long orderCardId, BigDecimal refundfree,BigDecimal totalfree,String desc,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        Payments p=payMentsService.selectPaments(orderCardId);
        Double total=totalfree.doubleValue();//订单金额
        Double refund=refundfree.doubleValue();//退款金额
//        logger.info("微信退款Id"+p.getPaymentsId());
        return payservice.orderrefund(p.getPaymentsId(),refund,total,desc);
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
        Payments payments1=payMentsService.selectPaments(ordersId);
        if (payments1!=null){
            int msg=Integer.valueOf(payments1.getPayStatus());
            switch (msg){
                case 1:
                String id=String.valueOf(payments1.getPaymentsId());
                payments.setPayApp("1");
                String total=String.valueOf(payments1.getPayTotal());
                payMentsService.updatePayMentsForCallback(payments);
                Alipay alipay=payservice.alipayOfOrder(String.valueOf(id),"订单支付",total,"订单支付宝支付",passback_params);
                return alipay.getNum();
                case 2:
                    return "0";
                case 3:
                    return "0";
            }

        }
        Member member=(Member)session.getAttribute("Student");
        ShopOrder shopOrder=payMentsService.selectOrder(ordersId);
        logger.info(shopOrder.toString());
        if (passback_params==null){
            passback_params="0";
        }
        if (!passback_params.equals("3")){
            payments.setPayApp("2");
            if (shopOrder.getDeliveryTotal()!=null){
                payments.setPayTotal(shopOrder.getOrderTotal().add(shopOrder.getDeliveryTotal()));
            }else {
                payments.setPayTotal(shopOrder.getOrderTotal());
            }
            payments.setPayName(shopOrder.getStuId());
            payments.setReceivName(shopOrder.getShopId());
            payments.setPayName(member.getMemberId());
            payments.setPayStatus("1");
            payments.setCreatTime(creatTime);
            payments.setPayDesc("订单支付");
            payments.setOrderId(ordersId);
        }
        else {
            AfterOrder afterOrder= payMentsService.selectAfterOrder(ordersId);
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
        Alipay alipay=payservice.alipayOfOrder(String.valueOf(id),"订单支付",total,"订单支付宝支付",passback_params);
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
        ShopOrder shopOrder=payMentsService.selectOrder(orderId);
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
