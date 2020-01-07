package cn.armylife.union.controler;

import cn.armylife.union.domian.TaobaoConfig;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.TbkFavorites;
import com.taobao.api.domain.UatmTbkItem;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class TaobaoController {
    Logger logger= LoggerFactory.getLogger(TaobaoController.class);

    /**
     * 选品库列表
     * @param pageNo 页数
     * @return
     * @throws ApiException
     */
    @RequestMapping("FavoritesGet")
    @ResponseBody
    public List<TbkFavorites> FavoritesGet(Long pageNo) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkUatmFavoritesGetRequest req=new TbkUatmFavoritesGetRequest();
        req.setPageNo(pageNo);
        req.setPageSize(20L);
        req.setFields("favorites_title,favorites_id,type");
        TbkUatmFavoritesGetResponse response=client.execute(req);
//        logger.info("返回类型:"+response);
        List<TbkFavorites> favorites=response.getResults();
        return favorites;
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
    public List<UatmTbkItem> FavoritesItemGet(Long pageNo,Long favoritesId) throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkUatmFavoritesItemGetRequest request=new TbkUatmFavoritesItemGetRequest();
        request.setPlatform(1L);
        request.setPageSize(20L);
        request.setAdzoneId(109916450366L);
        request.setUnid("jxkzr345");
        request.setFavoritesId(favoritesId);
        request.setPageNo(pageNo);
        request.setFields("coupon_remain_count,coupon_total_count,coupon_start_time,coupon_info,coupon_end_time,num_iid,title,coupon_click_url,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type");
        TbkUatmFavoritesItemGetResponse response=client.execute(request);
//        logger.info("商品列表:"+response.getBody());
        return response.getResults();
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
    public List<TbkDgMaterialOptionalResponse.MapData> MaterialOptional(String word) throws ApiException{
        TaobaoClient client = new DefaultTaobaoClient(TaobaoConfig.Url, TaobaoConfig.appkey, TaobaoConfig.secret);
        TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
        req.setAdzoneId(109916450366L);
        req.setQ(word);
        TbkDgMaterialOptionalResponse rsp = client.execute(req);
//        logger.info("物料搜索:"+rsp.getBody());
        return rsp.getResultList();

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
}
