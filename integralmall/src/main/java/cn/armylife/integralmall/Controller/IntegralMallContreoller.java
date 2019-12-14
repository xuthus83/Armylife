package cn.armylife.integralmall.Controller;


import cn.armylife.common.Domain.*;
import cn.armylife.common.Util.NumberID;
import cn.armylife.integralmall.Service.IntegralMallService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IntegralMallContreoller {

    Logger logger= LoggerFactory.getLogger(IntegralMallContreoller.class);

    @Autowired IntegralMallService integralMallService;

    @Value("${server.port}")
    Integer port;

    @RequestMapping("registerIntegral")
    @ResponseBody
    public int registerIntegral(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        IntegralUser integralUser=new IntegralUser();
        integralUser.setUserId(member.getMemberId());
        integralUser.setIntegral(0);
        return integralMallService.integralUserinsert(integralUser);
    }

    /**
     * 积分增加
     * @param integral
     * @param request
     * @return
     */
    @RequestMapping("integralIncrease")
    @ResponseBody
    public int integralIncrease(Integer integral, HttpServletRequest request){
        logger.info(integral+"uiuiui");
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        IntegralUser integralUser=new IntegralUser();
        integralUser.setUserId(member.getMemberId());
        integralUser.setIntegral(integral);
        return integralMallService.integralIncrease(integral,member.getMemberId());
    }

    /**
     * 积分减少
     * @param integral
     * @param request
     * @return
     */
    @RequestMapping("integralReduction")
    @ResponseBody
    public int integralReduction(Integer integral, HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        return integralMallService.integralReduction(integral,member.getMemberId());
    }

    /**
     * 通过用户Id查找用户积分findUserIntegral
     * @param request
     * @return
     */
    @RequestMapping("findUserIntegral")
    @ResponseBody
    public Integer findUserIntegral(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        IntegralUser integralUser=integralMallService.findUserIntegral(member.getMemberId());
        return integralUser.getIntegral();
    };

    /**
     * 添加兑换记录(订单)
     * @param pointsMallProductsId
     * @param request
     * @return
     */
    @RequestMapping("convertedCommodities")
    @ResponseBody
    public Long convertedCommodities(Long pointsMallProductsId,
                                     PointsExchangeRecord pointsExchangeRecord,
                                     Long productDetail,
                                     HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("Student");
        PointsMallProducts product = integralMallService.findMallProducts(pointsMallProductsId);
        Integer userIntegral=this.findUserIntegral(request);
        if (userIntegral<product.getUsingIntegral()){
            return 0L;
        }
        Long int1=NumberID.nextId(port);
        pointsExchangeRecord.setPointsExchangeRecordId(int1);
        pointsExchangeRecord.setUsingIntegral(product.getUsingIntegral());//兑换使用积分
        pointsExchangeRecord.setUsageAmount(product.getUsageAmount());//兑换使用金额
        pointsExchangeRecord.setUserId(member.getMemberId());
        pointsExchangeRecord.setItemsExchanged(product.getTradeName());//兑换商品名称
        pointsExchangeRecord.setProductId(pointsMallProductsId);
        pointsExchangeRecord.setProductDetailId(productDetail);
        Long recordId = integralMallService.exchangeRecordinsert(pointsExchangeRecord);//添加兑换记录
        integralMallService.integralReduction(product.getUsingIntegral(), member.getMemberId());//增加积分
        return int1;
    }

    /**
     * 查询所有商品分类
     * @return
     */
    @RequestMapping("mallAreaSelectAll")
    @ResponseBody
    public List<MallArea> mallareaselectAll(){
        return integralMallService.mallareaselectAll();
    };

    /**
     * 通过分区Id查看商品
     * @param respectiveArea
     * @return
     */
    @RequestMapping("findProductForArea")
    @ResponseBody
    public PageInfo<PointsMallProducts> findProductForArea(Long respectiveArea,int pageNum){
        PageHelper.startPage(pageNum, 10);
        PageInfo<PointsMallProducts> pageInfo=new PageInfo<>(integralMallService.findProductForArea(respectiveArea));
        return pageInfo;
    };

    /**
     * 查询兑换记录
     * @param request
     * @return
     */
    @RequestMapping("findExchangeRecord")
    @ResponseBody
    public PageInfo<PointsExchangeRecord> findExchangeRecord(HttpServletRequest request,int pageNum){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        PageHelper.startPage(pageNum, 10);
        PageInfo<PointsExchangeRecord> pageInfo=new PageInfo<>(integralMallService.findExchangeRecord(member.getMemberId()));
        return pageInfo;
    };

    /**
     * 通过商品Id查询信息
     * @param productId
     * @return
     */
        @RequestMapping("findProductForProductId")
    @ResponseBody
    public PointsMallProducts findProductForProductId(Long productId){
        return integralMallService.findProductForProductId(productId);
    };

    /**
     * 通过商品Id查询规格
     * @param productDetailId
     * @return
     */
    @RequestMapping("findProductForDetail")
    @ResponseBody
    public MallProductsDetail findProductForDetail(Long productDetailId){
        return integralMallService.findProductForDetail(productDetailId);
    };
}
