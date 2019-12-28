package cn.armylife.member.util;

import cn.armylife.member.domain.WXtemplate;
import cn.armylife.member.domain.WeChatUsers;
import cn.hutool.http.HttpUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageWechat {
    @Autowired
    WeChatUsers weChatUsers;


    public String token(int num){
        String appid="";
        String secret="";
        if (num==0){
            appid=weChatUsers.getAppId();
            secret=weChatUsers.getAppSecret();
        }else if (num==1){
            appid=weChatUsers.getAppId();
            secret=weChatUsers.getAppSecret();
        }
        String URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
        String getDataStr="&appid="+appid+"&secret="+secret;
        String str= TemplateMsgService.HttpGet(URL,getDataStr);
        JSONObject jsonObject=JSONObject.fromObject(str);
        String message=jsonObject.toString();
        System.out.println("token"+message);
        String token=(String) jsonObject.get("access_token");
        return token;
    }


    public String sendMessage(WXtemplate wXtemplate, int num){
        String accesstoken= token(num);
        String URL="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accesstoken;
        JSONObject wxmessage=new JSONObject();
        wxmessage.put("touser",wXtemplate.getOpenid());
        wxmessage.put("template_id",wXtemplate.getTemplate());
        wxmessage.put("url","https://www.oneoffer.cn/1/"+wXtemplate.getUrl());
        JSONObject fstJson=new JSONObject();
        fstJson.put("value",wXtemplate.getFirst());
        JSONObject keyword1=new JSONObject();
        keyword1.put("value",wXtemplate.getKey1());
        JSONObject keyword2=new JSONObject();
        keyword2.put("value",wXtemplate.getKey2());
        JSONObject remark=new JSONObject();
        remark.put("value",wXtemplate.getRemark1());
        JSONObject date=new JSONObject();
        date.put("first",fstJson);
        date.put("keyword1",keyword1);
        date.put("keyword2",keyword2);
        if (wXtemplate.getKey3()!=null){
            JSONObject keyword3=new JSONObject();
            keyword3.put("value",wXtemplate.getKey3());
            date.put("keyword3",keyword3);
        }if (wXtemplate.getKey4()!=null){
            JSONObject keyword4=new JSONObject();
            keyword4.put("value",wXtemplate.getKey4());
            date.put("keyword4",keyword4);
        }
        date.put("remark",remark);
        wxmessage.put("data",date.toString());
        String xmlStr= HttpUtil.post(URL,wxmessage.toString());
        System.out.println(xmlStr);
        return xmlStr;
    }

    public String newOrderService(WXtemplate wXtemplate,int num){
        String accesstoken= token(num);
        String URL="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accesstoken;
        JSONObject wxmessage=new JSONObject();
        wxmessage.put("touser",wXtemplate.getOpenid());
        wxmessage.put("template_id",wXtemplate.getTemplate());
        wxmessage.put("url","https://www.armylife.cn/"+wXtemplate.getUrl());
        JSONObject fstJson=new JSONObject();
        fstJson.put("value",wXtemplate.getFirst());
        JSONObject keyword1=new JSONObject();
        keyword1.put("value",wXtemplate.getKey1());
        JSONObject keyword2=new JSONObject();
        keyword2.put("value",wXtemplate.getKey2());
        JSONObject keyword3=new JSONObject();
        keyword2.put("value",wXtemplate.getKey3());
        JSONObject keyword4=new JSONObject();
        keyword2.put("value",wXtemplate.getKey4());
        JSONObject keyword5=new JSONObject();
        keyword2.put("value",wXtemplate.getKey5());
        JSONObject remark=new JSONObject();
        remark.put("value",wXtemplate.getRemark1());
        JSONObject date=new JSONObject();
        date.put("first",fstJson);
        date.put("keyword1",keyword1);
        date.put("keyword2",keyword2);
        date.put("keyword3",keyword2);
        date.put("keyword4",keyword2);
        date.put("keyword5",keyword2);
        date.put("remark",remark);
        wxmessage.put("data",date.toString());
        String xmlStr= HttpUtil.post(URL,wxmessage.toString());
        System.out.println(xmlStr);
        return xmlStr;
    }
}
