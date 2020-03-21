package cn.armylife.union.controler;

import cn.armylife.common.domain.Member;
import cn.armylife.common.domain.ShopOrder;
import cn.armylife.union.util.HttpUtil;
import cn.armylife.union.util.MapKeyComparator;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * creat by xuthus on 2020/3/10.
 **/
@Controller
@Slf4j
public class MeiTuanController {


    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

    @ResponseBody
    @RequestMapping("token")
    public JSONObject token(String code){
        String url="https://openapi.waimai.meituan.com/oauth/access_token?app_id=1115774341341459&secret=3aee80fa608c4bf09448111edf67442c";
        String para="&code="+code+"&grant_type=authorization_code";
        String msg= HttpUtil.sendPost(url,para);
        JSONObject jsonObject=JSONObject.fromObject(msg);
        return jsonObject;
    }

    @ResponseBody
    @RequestMapping("getOpenId")
    public JSONObject getOpenId(String code){
        String url="https://openapi.waimai.meituan.com/oauth/userinfo?app_id=1115774341341459&secret=3aee80fa608c4bf09448111edf67442c";
        String para="&access_token="+code;
        String msg= HttpUtil.sendPost(url,para);
        JSONObject jsonObject=JSONObject.fromObject(msg);
        return jsonObject;
    }

    /**
     * 获取商铺列表
     * @return
     * @throws Exception
     */
    @RequestMapping("poilist")
    @ResponseBody
    public JSONObject poilist(String latitude,String longitude,String openId) throws Exception{
        String url="https://openapi.waimai.meituan.com/openapi/v1/poilist?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Long time= System.currentTimeMillis()/1000;
        Map<String,String> date=new HashMap<>();
        date.put("latitude",latitude);//用户当前经度
        date.put("longitude",longitude);//用户当前纬度
        date.put("timestamp",time.toString());
        date.put("open_id",openId);
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 店铺商品列表
     * @param latitude
     * @param longitude
     * @param wmPoiId
     * @return
     * @throws Exception
     */
    @RequestMapping("food")
    @ResponseBody
    public JSONObject food(String latitude,String longitude,String wmPoiId) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/v1/poi/food?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> date=new HashMap<>();
        date.put("latitude",latitude);//用户当前经度
        date.put("longitude",longitude);//用户当前纬度
        date.put("timestamp",time.toString());
        date.put("wm_poi_id",wmPoiId);
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        System.out.println(signDate);
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }


    /**
     * 订单预览
     * @param latitude
     * @param longitude
     * @param wmPoiId
     * @return
     * @throws Exception
     */
    @RequestMapping("orderPreview")
    @ResponseBody
    public JSON orderPreview(String latitude,String longitude,String wmPoiId,HttpServletRequest request) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="hthttps://openapi.waimai.meituan.com/openapi/v1/order/preview?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> payload=new HashMap<>();
        Map<String,String> date=new HashMap<>();
        Map<String,String> orderingList=new HashMap<>();//订单信息
        orderingList.put("wm_poi_id","");
        orderingList.put("delivery_time","");
        orderingList.put("pay_type","2");

        List<Map> foodList=new ArrayList<>();
        String[] sku_ids=request.getParameterValues("skuId");
        String foods=new String();
        for(int i=0;i<sku_ids.length;i++){
            foods=sku_ids[i];
            String[] foodsList=foods.split("x");
            Map<String,String> food=new HashMap<>();//菜品信息
            food.put("wm_food_sku_id",foodsList[0]);
            food.put("count",foodsList[1]);
            foodList.add(food);
        }
        orderingList.put("food_list",foodList.toString());
        Map<String,String>  orderingUser=new HashMap<>();
        orderingUser.put("user_latitude","");
        orderingUser.put("user_longitude","");
        orderingUser.put("user_phone","");
        orderingUser.put("user_address","");
        orderingUser.put("user_name","");
        orderingUser.put("addr_latitude","");
        orderingUser.put("addr_longitude","");
        orderingUser.put("user_caution","");

        payload.put("wm_ordering_user","");
        date.put("latitude",latitude);//用户当前经度
        date.put("longitude",longitude);//用户当前纬度
        date.put("timestamp",time.toString());
        date.put("wm_poi_id",wmPoiId);
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 订单提交
     * @return
     */
    @RequestMapping("orderSubmit")
    @ResponseBody
    public Map<String, String> orderSubmit(HttpServletRequest request){
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/v1/order/submit?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> payload=new HashMap<>();
        Map<String,String> date=new HashMap<>();
        date.put("timestamp",time.toString());
        date.put("pay_source","3");
        Map<String,String> orderList=new HashMap<>();//订单信息
        orderList.put("wm_poi_id","2868090");
        orderList.put("delivery_time","3");
        orderList.put("pay_type","3");
        //获取菜品id数组
        List<Map> foodList=new ArrayList<>();
        String[] sku_ids=request.getParameterValues("skuId");
        String foods=new String();
        for(int i=0;i<sku_ids.length;i++){
            foods=sku_ids[i];
            String[] foodsList=foods.split("x");
            Map<String,String> food=new HashMap<>();//菜品信息
            food.put("wm_food_sku_id",foodsList[0]);
            food.put("count",foodsList[1]);
            foodList.add(food);
        }
        orderList.put("food_list",foodList.toString());
        Map<String,String> orderuser=new HashMap<>();//收餐人详细信息
        orderuser.put("user_longitude","3");
        orderuser.put("user_phone","3");
        orderuser.put("user_address","3");
        orderuser.put("user_name","3");
        orderuser.put("addr_latitude","3");
        orderuser.put("addr_longitude","3");
        orderuser.put("user_caution","3");
        orderuser.put("user_latitude","3");
        Map<String,String> wxPayParams=new HashMap<>();//微信小程序支付相关参数，传递该参数后，则按照微信小程序支付的方式进行下单
        wxPayParams.put("openid","3");
        wxPayParams.put("app_id","3");
        date.put("wm_ordering_list",orderList.toString());
        date.put("wm_ordering_user",orderuser.toString());
        date.put("wx_pay_params",wxPayParams.toString());
        date.put("token","3");
        date.put("food_list","3");
        payload.put("payload",date.toString());
        Map<String, String> resultMap = sortMapByKey(payload);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        date.put("json",jsonObject.toString());

        return date;
    }

    /**
     * 根据商家信息获取预计送达时间列表项
     * @param latitude
     * @param longitude
     * @param wmPoiId
     * @return
     * @throws Exception
     */
    @RequestMapping("arriveTime")
    @ResponseBody
    public JSON arriveTime(String latitude,String longitude,String wmPoiId) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/v1/poi/arriveTime?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> date=new HashMap<>();
        date.put("latitude",latitude);//用户当前经度
        date.put("longitude",longitude);//用户当前纬度
        date.put("timestamp",time.toString());
        date.put("wm_poi_id",wmPoiId);
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("orderCancel")
    @ResponseBody
    public JSON orderCancel(String orderId) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/v2/order/cancel?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> date=new HashMap<>();
        date.put("order_id",orderId);
        date.put("timestamp",time.toString());
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 获取订单列表
     * @param cursor
     * @return
     * @throws Exception
     */
    @RequestMapping("orderList")
    @ResponseBody
    public JSON orderList(String cursor) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/v1/order/list?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> date=new HashMap<>();
        date.put("cursor",cursor);//获取下一页内容的游标. 返回数据中hasMore为true时, 会返回请求下一页的游标.使用回传的游标, 可以查询下一页信息
        date.put("timestamp",time.toString());//时间戳
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping("orderQuery")
    @ResponseBody
    public JSON orderQuery(String orderId) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/v1/order/query?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> date=new HashMap<>();
        date.put("order_id",orderId);
        date.put("timestamp",time.toString());
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 查询商家详情
     * @param latitude
     * @param longitude
     * @param wmPoiId
     * @return
     * @throws Exception
     */
    @RequestMapping("poiDetailinfo")
    @ResponseBody
    public JSON poiDetailinfo(String latitude,String longitude,String wmPoiId) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/v2/poi/detailinfo?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> date=new HashMap<>();
        date.put("latitude",latitude);//用户当前经度
        date.put("longitude",longitude);//用户当前纬度
        date.put("timestamp",time.toString());
        date.put("wm_poi_id",wmPoiId);
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 更新&新增地址
     * @param latitude
     * @param longitude
     * @param name
     * @param gender
     * @param phone
     * @param address
     * @param addressId
     * @return
     * @throws Exception
     */
    @RequestMapping("addressUpdate")
    @ResponseBody
    public JSON addressUpdate(String latitude,String longitude,String name,String gender,String phone,String address,String addressId) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/address/update?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> date=new HashMap<>();
        date.put("latitude",latitude);//用户当前经度
        date.put("longitude",longitude);//用户当前纬度
        date.put("timestamp",time.toString());
        date.put("name",name);//收货人名称
        date.put("gender",gender);//	性别 1：男 2：女
        date.put("phone",phone);//	手机号
        date.put("address",address);//地址
        date.put("addressId",addressId);//地址id(该参数存在时为更新操作)
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 	获取用户收获地址
     * @param wmPoiId
     * @return
     * @throws Exception
     */
    @RequestMapping("addressQueryList")
    @ResponseBody
    public JSON addressQueryList(String wmPoiId) throws Exception{
        Long time= System.currentTimeMillis()/1000;
        String url="https://openapi.waimai.meituan.com/openapi/address/queryList?app_id=1115774341341459";
        String secret="3aee80fa608c4bf09448111edf67442c";
        Map<String,String> date=new HashMap<>();
        date.put("wmPoiId",wmPoiId);//门店id
        date.put("timestamp",time.toString());
        Map<String, String> resultMap = sortMapByKey(date);
        String sign=HttpUtil.getSign(resultMap,url,secret);
        date.put("sign",sign);
        String signDate = "";
        for (Map.Entry<String,String> entry : date.entrySet()){
            String dates="&"+entry.getKey()+"="+entry.getValue();
            signDate+=dates;
        }
        String data=HttpUtil.sendPost(url,signDate);
        JSONObject jsonObject=JSONObject.fromObject(data);
        return jsonObject;
    }

    /**
     * 商品添加购物车时计算价格
     * @param longitude
     * @param latitude
     * @param wmPoiId
     * @param request
     * @param count
     * @return
     * @throws Exception
     */
    @RequestMapping("calculateprice")
    @ResponseBody
    public JSON calculateprice(String longitude,String latitude,String wmPoiId,String count,HttpServletRequest request) throws Exception {
        Long time = System.currentTimeMillis() / 1000;
        String url = "https://openapi.waimai.meituan.com/openapi/v2/shoppingcart/calculateprice?app_id=1115774341341459";
        String secret = "3aee80fa608c4bf09448111edf67442c";
        Map<String, String> date = new HashMap<>();

        date.put("latitude", latitude);//用户当前经度
        date.put("longitude", longitude);//用户当前纬度

        Map<String, String> price_input = new HashMap<>();//菜品SKU信息
        price_input.put("wmPoiId", wmPoiId);//外卖门店id
        //获取菜品id数组
        List<Map> foodList = new ArrayList<>();
        String[] sku_ids = request.getParameterValues("skuId");
        String foods = new String();
        for (int i = 0; i < sku_ids.length; i++) {
            foods = sku_ids[i];
            String[] foodsList = foods.split("x");
            Map<String, String> food = new HashMap<>();//菜品信息
            food.put("skuId", foodsList[0]);//	菜品SKU-ID
            food.put("count", foodsList[1]);//菜品数量
            foodList.add(food);
        }
        price_input.put("skuList", foodList.toString());
        date.put("calculate_price_input", "29735952");
        date.put("timestamp", time.toString());

        Map<String, String> resultMap = sortMapByKey(date);

        String sign = HttpUtil.getSign(resultMap, url, secret);
        String signDate = "";
        for (Map.Entry<String, String> entry : date.entrySet()) {
            String dates = "&" + entry.getKey() + "=" + entry.getValue();
            signDate += dates;
        }
        String data = HttpUtil.sendPost(url, signDate);
        JSONObject jsonObject = JSONObject.fromObject(data);
        return jsonObject;
    }



}
