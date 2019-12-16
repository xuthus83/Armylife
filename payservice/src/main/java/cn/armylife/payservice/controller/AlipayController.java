package cn.armylife.payservice.controller;

import cn.armylife.common.config.AlipayConfig;
import cn.armylife.common.util.NumberID;
import cn.armylife.payservice.domain.Alipay;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePageRefundModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import static cn.armylife.payservice.config.AlipayConfig.CHARSET;

@Controller
public class AlipayController {

    @Value("${server.port}")
    int port;
    Logger logger=Logger.getLogger("AlipayController.class");


    @RequestMapping("/alipayOrder")
    @ResponseBody
    public Alipay alipay(HttpServletRequest request, HttpServletResponse httpServletResponse,String WIDout_trade_no, String WIDsubject, String WIDtotal_amount, String WIDbody,String passback_params) throws AlipayApiException, IOException {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no =WIDout_trade_no;//String.valueOf( NumberID.nextId(port));//  new String(request.getParameter("WIDout_trade_no"));
        logger.info("订单号"+out_trade_no);
        // 订单名称，必填
        String subject = WIDsubject;//new String(request.getParameter("WIDsubject"));
        // 付款金额，必填
        String total_amount= WIDtotal_amount;//new String(request.getParameter("WIDtotal_amount"));
        // 商品描述，可空
        String body = WIDbody;// new String(request.getParameter("WIDbody"));
        // 超时时间 可空n
        String timeout_express="15m";
        // 销售产品码 必填
        String product_code="FAST_INSTANT_TRADE_PAY";//"QUICK_WAP_PAY";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:ss");
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 90);
        String time_expire=sdf.format(nowTime.getTime());
        logger.info(time_expire);
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
//        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
        AlipayTradePagePayRequest alipay_request = new AlipayTradePagePayRequest();
        // 封装请求支付信息
        AlipayTradePagePayModel model=new AlipayTradePagePayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        model.setPassbackParams(passback_params);
//        model.setTimeExpire(time_expire);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.return_url);

        alipay_request.getApiMethodName();

        AlipayTradePagePayResponse response=null;

        try {
            response=client.pageExecute(alipay_request,"get");
        }catch (AlipayApiException e){
            e.printStackTrace();
        }
        logger.info("返回数据:"+response.getBody());
        if (response.isSuccess()) {
            logger.info("调用成功");
        }else {
            logger.info("调用失败");
        }
        Alipay alipay=new Alipay();
        alipay.setNum(response.getBody());
        return alipay;
}

    @RequestMapping("/getIndex")
    public String getIndex(){
        logger.info("使用完成");
        return "/index.html";
    }

    @RequestMapping("AlipayCallback")
    @ResponseBody
    public Map<String, String> AlipayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException,AlipayApiException{
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

        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("商户订单号"+out_trade_no);
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info(  "支付宝交易号"+trade_no);

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

        boolean verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式


        if (verify_result){
            response.getOutputStream().println("success");	//请不要修改或删除
            response.getOutputStream().close();
        }else {
            response.getOutputStream().println("fail");
            return params;

        }
        return params;
    }


    @RequestMapping("/Alipayrefund")
    @ResponseBody
    public Alipay Alipayrefund(HttpServletRequest request, HttpServletResponse httpServletResponse,String WIDout_trade_no, String WIDsubject, String WIDrefund_amount, String WIDbody) throws AlipayApiException, IOException {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no =WIDout_trade_no;//String.valueOf( NumberID.nextId(port));//  new String(request.getParameter("WIDout_trade_no"));
        logger.info("订单号"+out_trade_no);
        // 退款说明，必填
        String subject = WIDsubject;//new String(request.getParameter("WIDsubject"));
        // 退款金额，必填
        String refund_amount= WIDrefund_amount;//new String(request.getParameter("WIDtotal_amount"));
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:ss");
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 90);
        String time_expire=simpleDateFormat.format(nowTime.getTime());
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
//        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
        AlipayTradePageRefundRequest alipay_request = new AlipayTradePageRefundRequest();
        // 封装请求支付信息
        AlipayTradePageRefundModel model=new AlipayTradePageRefundModel();
        model.setOutTradeNo(out_trade_no);
        model.setRefundReason(subject);
        model.setRefundAmount(refund_amount);
//        model.setTimeExpire(time_expire);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl("http://www.xuthus83.cn:6081/PayMents/AlipayRefundCallback");
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.refund_url);

        alipay_request.getApiMethodName();

        AlipayTradePageRefundResponse response=null;

        try {
            response=client.pageExecute(alipay_request,"get");
        }catch (AlipayApiException e){
            e.printStackTrace();
        }
        logger.info("返回数据:"+response.getBody());
        if (response.isSuccess()) {
            logger.info("退款提交成功成功");
        }else {
            logger.info("退款提交失败失败");
        }
        Alipay alipay=new Alipay();
        alipay.setNum(response.getBody());
        return alipay;
    }

    @RequestMapping("AlipayRefundCallback")
    @ResponseBody
    public int AlipayRefundCallback(HttpServletRequest request, HttpServletResponse response) throws IOException,AlipayApiException{
        return 0;
    }

    @RequestMapping("/transfer")
    @ResponseBody
    public int transfer(HttpServletResponse response, HttpServletRequest request, BigDecimal amount, String identity, String order_title) throws AlipayApiException,IOException {
        String out_biz_no = String.valueOf(NumberID.nextId(port));
        String product_code = "TRANS_ACCOUNT_NO_PWD";
        String biz_scene = "DIRECT_TRANSFER";
        String payer_show_name = "沙箱环境";
        String trans_amount = String.valueOf(amount);
        logger.info("进入提现");

        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        Participant participant = new Participant();
        participant.setIdentity(identity);
        participant.setIdentityType("ALIPAY_LOGON_ID");
        participant.setName(payer_show_name);
        AlipayFundTransUniTransferModel uniTransferModel = new AlipayFundTransUniTransferModel();
        uniTransferModel.setOutBizNo(out_biz_no);
        uniTransferModel.setProductCode(product_code);
//        uniTransferModel.setBusinessParams(biz_scene);
        uniTransferModel.setOrderTitle(order_title);
        uniTransferModel.setTransAmount(trans_amount);
        uniTransferModel.setPayeeInfo(participant);
        uniTransferModel.setRemark("转账");
        uniTransferModel.setBizScene(biz_scene);

        AlipayFundTransUniTransferRequest uniTransferRequest = new AlipayFundTransUniTransferRequest();
        uniTransferRequest.setBizModel(uniTransferModel);
        uniTransferRequest.setNotifyUrl("http://www.xuthus83.cn:6081/PayMents/AlipayRefundCallback");
        uniTransferRequest.setReturnUrl("http://www.xuthus83.cn:6081/PayMents/AlipayRefundCallback");
        AlipayFundTransUniTransferResponse response1 = null;
        try {
            response1 = client.execute(uniTransferRequest, "get");
        } catch (AlipayApiException e) {
            e.printStackTrace();
            logger.info("错误:" + e);
        }
        if(response1.isSuccess()){
            return 1;
        }else {
            return 0;
        }
    }

    @RequestMapping("AlipayAccessToken")
    @ResponseBody
    public Map<Integer,String> AliPayUserInfo(String auth_code) throws AlipayApiException{
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipaySystemOauthTokenRequest tokenRequest=new AlipaySystemOauthTokenRequest();
        tokenRequest.setCode(auth_code);
        tokenRequest.setGrantType("authorization_code");

        Map<Integer,String> msg=new HashMap<>();
        AlipaySystemOauthTokenResponse  tokenResponse=client.execute(tokenRequest);
            logger.info("token:"+tokenResponse);
            logger.info("AccessToken:"+tokenResponse.getAccessToken());
        //调用接口获取用户信息
        AlipayUserInfoShareRequest requestUser = new AlipayUserInfoShareRequest();
        try {
            AlipayUserInfoShareResponse userinfoShareResponse = client.execute(requestUser, tokenResponse.getAccessToken());
            if(userinfoShareResponse.isSuccess()){
                System.out.print(userinfoShareResponse.getBody());
                System.out.println("调用成功");
                System.out.print("UserId:" + userinfoShareResponse.getUserId() + "\n");//用户支付宝ID
                System.out.print("NickName:" + userinfoShareResponse.getNickName() + "\n");//用户支付宝昵称
                System.out.print("UserType:" + userinfoShareResponse.getUserType() + "\n");//用户类型
                System.out.print("UserStatus:" + userinfoShareResponse.getUserStatus() + "\n");//用户账户动态
                System.out.print("Email:" + userinfoShareResponse.getEmail() + "\n");//用户Email地址
                System.out.print("IsCertified:" + userinfoShareResponse.getIsCertified() + "\n");//用户是否进行身份认证
                System.out.print("IsStudentCertified:" + userinfoShareResponse.getIsStudentCertified() + "\n");//用户是否进行学生认证
            } else {
                System.out.println("调用失败");
            }

        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
        }
        return null;
    }


}
