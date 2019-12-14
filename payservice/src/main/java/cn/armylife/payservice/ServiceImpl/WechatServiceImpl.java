package cn.armylife.payservice.ServiceImpl;

import cn.armylife.payservice.Domain.WeChatUsers;
import cn.armylife.payservice.Service.WechatService;
import cn.armylife.payservice.Utils.HttpGet;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class WechatServiceImpl implements WechatService {
    Logger logger=Logger.getLogger("WecechatService.service");

    @Autowired
    WeChatUsers weChatUsers;

    @Override
    public String getOpenId(String code) {
        String result=null;
        try {
            String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";
            String getDataStr = "&appid=" + weChatUsers.getAppId() + "&secret=" + weChatUsers.getAppSecret() + "&code="+code;
            String str = HttpGet.HttpGet(URL, getDataStr);
            JSONObject json= JSONObject.parseObject(str);
            String openid=(String) json.get("openid");
//            openid=json.toString();
            if (openid!=null){
                result=openid;
            }else {
                result="获取失败";
            }

        }catch (Exception e){
            logger.info(e+"");
        }
        logger.info(result);
        return result;
    }

    @Override
    public Map<Integer, String> getUserInfo(String code) {
        String URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
        String getDataStr = "&appid=" + "wx0f1cabff22920c83" + "&secret=" + "1c3a6dc03fafa955fe50dc867a4ed5a1";
        String str = HttpGet.HttpGet(URL, getDataStr);
        JSONObject json = JSONObject.parseObject(str);
        String access_token=(String) json.get("access_token");
        String openid=(String) json.get("openid");
        String URL2 = "https://api.weixin.qq.com/cgi-bin/user/info?";
        String getDataStr2 = "&access_token=" + access_token + "&openid=" + openid+"&lang="+"zh_CN";
        String str2 = HttpGet.HttpGet(URL2, getDataStr2);
        JSONObject json2 = JSONObject.parseObject(str2);
        String user1=(String) json2.get("headimgurl");//toString();
        String user2=user1.replaceFirst("http://","https://");
        Map<Integer,String> msg=new HashMap<>();
        msg.put(1,user2);
        msg.put(2,openid);
        msg.put(3,str2);
        return msg;
    }
}
