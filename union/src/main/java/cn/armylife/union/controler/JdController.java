package cn.armylife.union.controler;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import jd.union.open.category.goods.get.request.CategoryReq;
import jd.union.open.category.goods.get.request.UnionOpenCategoryGoodsGetRequest;
import jd.union.open.category.goods.get.response.CategoryResp;
import jd.union.open.category.goods.get.response.UnionOpenCategoryGoodsGetResponse;
import jd.union.open.goods.bigfield.query.request.BigFieldGoodsReq;
import jd.union.open.goods.bigfield.query.request.UnionOpenGoodsBigfieldQueryRequest;
import jd.union.open.goods.bigfield.query.response.BigFieldGoodsResp;
import jd.union.open.goods.bigfield.query.response.UnionOpenGoodsBigfieldQueryResponse;
import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
import jd.union.open.goods.jingfen.query.request.UnionOpenGoodsJingfenQueryRequest;
import jd.union.open.goods.jingfen.query.response.JFGoodsResp;
import jd.union.open.goods.jingfen.query.response.UnionOpenGoodsJingfenQueryResponse;
import jd.union.open.goods.promotiongoodsinfo.query.request.UnionOpenGoodsPromotiongoodsinfoQueryRequest;
import jd.union.open.goods.promotiongoodsinfo.query.response.PromotionGoodsResp;
import jd.union.open.goods.promotiongoodsinfo.query.response.UnionOpenGoodsPromotiongoodsinfoQueryResponse;
import jd.union.open.goods.query.request.GoodsReq;
import jd.union.open.goods.query.request.UnionOpenGoodsQueryRequest;
import jd.union.open.goods.query.response.GoodsResp;
import jd.union.open.goods.query.response.UnionOpenGoodsQueryResponse;
import jd.union.open.goods.stuprice.query.request.StuPriceGoodsReq;
import jd.union.open.goods.stuprice.query.request.UnionOpenGoodsStupriceQueryRequest;
import jd.union.open.goods.stuprice.query.response.StuPriceGoodsResp;
import jd.union.open.goods.stuprice.query.response.UnionOpenGoodsStupriceQueryResponse;
import jd.union.open.position.query.request.PositionReq;
import jd.union.open.position.query.request.UnionOpenPositionQueryRequest;
import jd.union.open.position.query.response.PositionQueryResp;
import jd.union.open.position.query.response.UnionOpenPositionQueryResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * creat by xuthus on 2020/3/7.
 **/
@Controller
@RequestMapping("JdService")
public class JdController {

    /**
     * 京粉精选商品查询
     * @return
     * @throws JdException
     */
    @RequestMapping("JFGoodsResp")
    @ResponseBody
    public JFGoodsResp[] JFGoodsResp() throws JdException {
        JdClient client=new DefaultJdClient( "https://router.jd.com/api","","cc7ba04f6ed7f32f758da4c9c0949a40","867b278433674a2aaa112afe7fee53e7");
        UnionOpenGoodsJingfenQueryRequest request=new UnionOpenGoodsJingfenQueryRequest();
        JFGoodsReq goodsReq=new JFGoodsReq();
        goodsReq.setEliteId(1);
        request.setGoodsReq(goodsReq);
        UnionOpenGoodsJingfenQueryResponse response=client.execute(request);
        return response.getData();
    }

    /**
     * 关键词商品查询接口
     * @return
     * @throws JdException
     */
    @RequestMapping("GoodsQuery")
    @ResponseBody
    public UnionOpenGoodsQueryResponse GoodsQuerys(String name) throws JdException {
        JdClient client=new DefaultJdClient("https://router.jd.com/api","","cc7ba04f6ed7f32f758da4c9c0949a40","867b278433674a2aaa112afe7fee53e7");
        UnionOpenGoodsQueryRequest request=new UnionOpenGoodsQueryRequest();
        GoodsReq goodsReqDTO=new GoodsReq();
        goodsReqDTO.setKeyword(name);
        request.setGoodsReqDTO(goodsReqDTO);
        UnionOpenGoodsQueryResponse response=client.execute(request);
        return response;
    }

    /**
     * 获取推广商品信息
     * @param skuIds
     * @return
     * @throws JdException
     */
    @RequestMapping("promotiongoodsinfo")
    @ResponseBody
    public PromotionGoodsResp[] promotiongoodsinfo(String skuIds) throws JdException {
        JdClient client=new DefaultJdClient("https://router.jd.com/api","","cc7ba04f6ed7f32f758da4c9c0949a40","867b278433674a2aaa112afe7fee53e7");
        UnionOpenGoodsPromotiongoodsinfoQueryRequest request=new UnionOpenGoodsPromotiongoodsinfoQueryRequest();
        request.setSkuIds(skuIds);
        UnionOpenGoodsPromotiongoodsinfoQueryResponse response=client.execute(request);
        return response.getData();
    }

    /**
     * 学生价商品查询
     * @return
     * @throws JdException
     */
    @RequestMapping("stupriceQquery")
    @ResponseBody
    public StuPriceGoodsResp[] stupriceQquery() throws JdException {
        JdClient client=new DefaultJdClient("https://router.jd.com/api","","cc7ba04f6ed7f32f758da4c9c0949a40","867b278433674a2aaa112afe7fee53e7");
        UnionOpenGoodsStupriceQueryRequest request=new UnionOpenGoodsStupriceQueryRequest();
        StuPriceGoodsReq goodsReq=new StuPriceGoodsReq();
        request.setGoodsReq(goodsReq);
        UnionOpenGoodsStupriceQueryResponse response=client.execute(request);
        return response.getData();
    }

    /**
     * 商品类目查询
     * @param parentId
     * @param grade
     * @return
     * @throws JdException
     */
    @RequestMapping("categoryGoodsGet")
    @ResponseBody
    public CategoryResp[] categoryGoodsGet(Integer parentId, int grade) throws JdException {
        JdClient client=new DefaultJdClient("https://router.jd.com/api","","cc7ba04f6ed7f32f758da4c9c0949a40","867b278433674a2aaa112afe7fee53e7");
        UnionOpenCategoryGoodsGetRequest request=new UnionOpenCategoryGoodsGetRequest();
        CategoryReq req=new CategoryReq();
        req.setGrade(grade);
        req.setParentId(parentId);
        request.setReq(req);
        UnionOpenCategoryGoodsGetResponse response=client.execute(request);
        return response.getData();
    }

    /**
     * 商品详情查询
     * @param skuIds
     * @return
     * @throws JdException
     */
    @RequestMapping("bigfieldQuery")
    @ResponseBody
    public BigFieldGoodsResp[] bigfieldQuery(Long[] skuIds ) throws JdException {
        JdClient client=new DefaultJdClient("https://router.jd.com/api","","cc7ba04f6ed7f32f758da4c9c0949a40","867b278433674a2aaa112afe7fee53e7");
        UnionOpenGoodsBigfieldQueryRequest request=new UnionOpenGoodsBigfieldQueryRequest();
        BigFieldGoodsReq goodsReq=new BigFieldGoodsReq();
        goodsReq.setSkuIds(skuIds);
        request.setGoodsReq(goodsReq);
        UnionOpenGoodsBigfieldQueryResponse response=client.execute(request);
        return response.getData();
    }

    /**
     * 查询推广位
     * @param skuIds
     * @return
     * @throws JdException
     */
    @RequestMapping("positionQuery")
    @ResponseBody
    public UnionOpenPositionQueryResponse positionQuery(Long[] skuIds ) throws JdException {
        JdClient client=new DefaultJdClient("https://router.jd.com/api","","cc7ba04f6ed7f32f758da4c9c0949a40","867b278433674a2aaa112afe7fee53e7");
        UnionOpenPositionQueryRequest request=new UnionOpenPositionQueryRequest();
        PositionReq positionReq=new PositionReq();
        request.setPositionReq(positionReq);
        UnionOpenPositionQueryResponse response=client.execute(request);
        return response;
    }
}
