package cn.armylife.integralmall.service;

import cn.armylife.common.domain.*;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface IntegralMallService {

    /**
     * 添加用户初始积分数据
     * @param integralUser
     * @return
     */
    int integralUserinsert(IntegralUser integralUser);

    /**
     * 增加积分
     * @param integral
     * @param userId
     * @return
     */
    int integralIncrease(Integer integral,Long userId);

    /**
     * 减少积分
     * @param integral
     * @param userId
     * @return
     */
    int integralReduction(Integer integral,Long userId);

    /**
     * 通过用户Id查找用户积分
     * @param userId
     * @return
     */
    IntegralUser findUserIntegral(Long userId);

    /**
     * 通过积分商品Id获取积分商品信息
     * @param pointsMallProductsId
     * @return
     */
    PointsMallProducts findMallProducts(Long pointsMallProductsId);


    /**
     * 添加商品兑换记录
     * @param record
     * @return
     */
    Long exchangeRecordinsert(PointsExchangeRecord record);

    /**
     * 通过Id查询商品规格信息
     * @param productDetailId
     * @return
     */
    MallProductsDetail findProductForDetail(Long productDetailId);

    /**
     * 查询所有商城分类
     * @return
     */
    List<MallArea> mallareaselectAll();

    /**
     * 通过分区Id查看商品
     * @param respectiveArea
     * @return
     */
    Page<PointsMallProducts> findProductForArea(Long respectiveArea);

    /**
     * 查询兑换记录
     * @param userId
     * @return
     */
    Page<PointsExchangeRecord> findExchangeRecord(Long userId);

    /**
     * 通过商品Id查询信息
     * @param productId
     * @return
     */
    PointsMallProducts findProductForProductId(Long productId);

    /**
     * 通过Id查询兑换记录
     * @param pointsExchangeRecordId
     * @return
     */
    PointsExchangeRecord findExchangeRecordForId(Long pointsExchangeRecordId);

    /**
     * 添加支付记录
     * @param payments
     * @return
     */
    int paymentsinsert(Payments payments);


}
