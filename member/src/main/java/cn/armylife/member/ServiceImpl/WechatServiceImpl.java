package cn.armylife.member.ServiceImpl;

import cn.armylife.member.Domain.WeChatUsers;
import cn.armylife.member.Service.WechatService;
import cn.armylife.member.Util.HttpGet;
import net.sf.json.JSONObject;
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
        logger.info("微信公众数据:"+weChatUsers.toString());
        String result=null;
        try {
            String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";
            String getDataStr = "&appid=" + weChatUsers.getAppId() + "&secret=" +weChatUsers.getAppSecret() + "&code="+code;
            String str = HttpGet.HttpGet(URL, getDataStr);
            JSONObject json= JSONObject.fromObject(str);
            String openid=(String) json.get("openid");
//            openid=json.toString();
            if (!openid.equals(null)){
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
        logger.info("微信公众数据:"+weChatUsers.toString());
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
        String getDataStr = "appid=" + weChatUsers.getAppId() + "&secret=" + weChatUsers.getAppSecret() + "&code="+code+"&grant_type=authorization_code";
        String str = HttpGet.HttpGet(URL, getDataStr);
        logger.info("str"+str);
        JSONObject json = JSONObject.fromObject(str);
        String access_token=(String) json.get("access_token");
        logger.info("access_token:"+access_token);
        String openid=(String) json.get("openid");
        logger.info("openid:"+openid);
        String URL2 = "https://api.weixin.qq.com/sns/userinfo?";
        String getDataStr2 = "&access_token=" + access_token + "&openid=" + openid+"&lang="+"zh_CN";
        String str2 = HttpGet.HttpGet(URL2, getDataStr2);
        logger.info("str2"+str2);
        JSONObject json2 = JSONObject.fromObject(str2);

        String user1=(String) json2.get("headimgurl");//toString();
        logger.info(user1);
        String user2=user1.replaceFirst("http://","https://");
        Map<Integer,String> msg=new HashMap<>();
        msg.put(1,user2);
        msg.put(2,openid);
        msg.put(3,str2);
        return msg;
    }
}
