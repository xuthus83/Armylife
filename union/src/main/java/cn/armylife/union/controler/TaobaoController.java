package cn.armylife.union.controler;

import cn.armylife.union.domain.Favorites;
import cn.armylife.union.domain.TaobaoConfig;
import cn.armylife.union.service.TaobaoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.TbkFavorites;
import com.taobao.api.domain.UatmTbkItem;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
public class TaobaoController {
    Logger logger= LoggerFactory.getLogger(TaobaoController.class);
    @Autowired
    private TaobaoService taobaoService;

    /**
     * 选品库列表
     * @param pageNo 页数
     * @return
     * @throws ApiException
     */
    @RequestMapping("FavoritesGet")
    @ResponseBody
    public List<Favorites> FavoritesGet(Long pageNo) throws ApiException {
        return taobaoService.FavoritesAll();
    }

    /**
     * 同步选品库列表
     * @param pageNo 页数
     * @return
     * @throws ApiException
     */
    @RequestMapping("AutoGet")
    @ResponseBody
    public int AutoGet(Long pageNo) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkUatmFavoritesGetRequest req=new TbkUatmFavoritesGetRequest();
        req.setPageNo(pageNo);
        req.setPageSize(200L);
        req.setFields("favorites_title,favorites_id,type");
        TbkUatmFavoritesGetResponse response=client.execute(req);
//        logger.info("返回类型:"+response);
        List<TbkFavorites> favorites=response.getResults();
        taobaoService.deleteAll();
        Long num=response.getTotalResults();
        for (int i=0;i<favorites.size();i++){
            TbkFavorites tbkFavorites= favorites.get(i);
            JSONObject jsonObject=JSONObject.fromObject(tbkFavorites);
            Favorites favorites1=(Favorites) JSONObject.toBean(jsonObject,Favorites.class);
            logger.info("处理后json:"+favorites1.toString());
            taobaoService.FavoritesInsert(favorites1);
        }
        Favorites favorites1=new Favorites();
        favorites1.setFavoritesId(1L);
        favorites1.setFavoritesTitle("精选");
        favorites1.setType(1L);
        taobaoService.FavoritesInsert(favorites1);
        return 1;
    }

    /**
     * 选品库详情
     * @param pageNo 显示页数
     * @param favoritesId  选品库Id
     * @return
     * @throws ApiException
     */
    @RequestMapping("FavoritesItemGet")
    @ResponseBody
    public List<UatmTbkItem> FavoritesItemGet(Long pageNo, Long favoritesId) throws ApiException{
        String json=null;
        List<UatmTbkItem> msg=null;
        if (favoritesId==1){
            PageHelper.startPage(pageNo.intValue(), 20);
            PageInfo<UatmTbkItem> pageInfo=new PageInfo<>(taobaoService.FavoritesItemAll());
            List<UatmTbkItem> uatmTbkItemList=pageInfo.getList();
            msg=uatmTbkItemList;
        }else {
            TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
            TbkUatmFavoritesItemGetRequest request = new TbkUatmFavoritesItemGetRequest();
            request.setPlatform(1L);
            request.setPageSize(20L);
            request.setAdzoneId(109916450366L);
            request.setUnid("jxkzr345");
            request.setFavoritesId(favoritesId);
            request.setPageNo(pageNo);
            request.setFields("coupon_remain_count,coupon_total_count,coupon_start_time,coupon_info,coupon_end_time,num_iid,title,coupon_click_url,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type");
            TbkUatmFavoritesItemGetResponse response = client.execute(request);
            List<UatmTbkItem> uatmTbkItems = response.getResults();
            msg=uatmTbkItems;
        }
//        logger.info("商品列表:"+response.getBody());
        return msg;
    }

    /**
     * 商品详情
     * @param ids //商品id
     * @return
     * @throws ApiException
     */
    @RequestMapping("ItemInfoGet")
    @ResponseBody
    public List<TbkItemInfoGetResponse.NTbkItem> ItemInfoGet(String ids) throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkItemInfoGetRequest info=new TbkItemInfoGetRequest();
        info.setNumIids(ids);
        info.setPlatform(2L);
//        info.setIp("129.211.72.16");
        TbkItemInfoGetResponse response=client.execute(info);
//        logger.info("商品详情:"+response.getBody());
        return response.getResults();
    }

    /**
     * 搜索
     * @param word//物料搜索关键字
     * @return
     * @throws ApiException
     */
    @RequestMapping("MaterialOptional")
    @ResponseBody
    public List<TbkDgMaterialOptionalResponse.MapData> MaterialOptional(String word,Long pageNo) throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        req.setAdzoneId(109916450366L);
        req.setQ(word);
        req.setMaterialId(17004L);
        req.setPageNo(pageNo);
        req.setPlatform(2L);
        req.setNeedPrepay(true);
        req.setHasCoupon(true);
        TbkDgMaterialOptionalResponse rsp = client.execute(req);
//        logger.info("物料搜索:"+rsp.getBody());
        List<TbkDgMaterialOptionalResponse.MapData> mapDataList=rsp.getResultList();
        for (int i=0;i<mapDataList.size();i++){
            TbkDgMaterialOptionalResponse.MapData mapData=mapDataList.get(i);
            Long numIid=mapData.getNumIid();
            String activityId=mapData.getCouponId();
            String amount=this.CouponGet("",numIid,activityId,1);
            mapData.setCouponInfo(amount);
        }
        return rsp.getResultList();
    }

    @RequestMapping("spreadGet")
    @ResponseBody
    public String spreadGet(String tbkUrl) throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkSpreadGetRequest request=new TbkSpreadGetRequest();
        List<TbkSpreadGetRequest.TbkSpreadRequest> list2 = new ArrayList<TbkSpreadGetRequest.TbkSpreadRequest>();
        TbkSpreadGetRequest.TbkSpreadRequest obj3 = new TbkSpreadGetRequest.TbkSpreadRequest();
        obj3.setUrl(tbkUrl);
        list2.add(obj3);
        request.setRequests(list2);
        TbkSpreadGetResponse response=client.execute(request);
        return response.getBody();
    }

    /**
     * 淘口令
     * @param tbktext //口令弹框内容
     * @param tbkurl  //口令跳转url
     * @return
     * @throws ApiException
     */
    @RequestMapping("TpwdCreate")
    @ResponseBody
    public String TpwdCreate(String tbktext,String tbkurl) throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkTpwdCreateRequest request=new TbkTpwdCreateRequest();
        request.setText(tbktext);
        request.setUrl(tbkurl);
        TbkTpwdCreateResponse response=client.execute(request);
        return response.getData().getModel();
    }


    /**
     * 物料精选
     * @param pageNo
     * @return
     * @throws ApiException
     */
    @RequestMapping("OptimusMaterial")
    @ResponseBody
    public List<TbkDgOptimusMaterialResponse.MapData> OptimusMaterial(Long pageNo) throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkDgOptimusMaterialRequest request=new TbkDgOptimusMaterialRequest();
        request.setPageNo(pageNo);
        request.setAdzoneId(109916450366L);
        request.setMaterialId(9660L);
        TbkDgOptimusMaterialResponse response=client.execute(request);
        return response.getResultList();
    }

    /**
     * 券信息
     * @param numIid 商品ID
     * @param me 商品与券加密字符串
     * @param activityId 券ID
     * @param type 搜索判断
     * @return
     * @throws ApiException
     */
    @RequestMapping("CouponGet")
    @ResponseBody
    public String CouponGet(String me,Long numIid,String activityId,int type)throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkCouponGetRequest request=new TbkCouponGetRequest();
        if (type==1){
            request.setItemId(numIid);
            request.setActivityId(activityId);
        }else {
            request.setMe(me);
        }
        TbkCouponGetResponse response=client.execute(request);
        if (response.isSuccess()) {
            return response.getData().getCouponAmount();
        }
        return "";
    }
}
