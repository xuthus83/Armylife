package cn.armylife.integralmall.serviceimpl;

import cn.armylife.common.domain.*;
import cn.armylife.integralmall.mapper.*;
import cn.armylife.integralmall.service.IntegralMallService;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class IntegralMallServiceImpl implements IntegralMallService {

    Logger logger= LoggerFactory.getLogger(IntegralMallServiceImpl.class);

    @Autowired
    IntegralUserMapper integralUserMapper;
    @Autowired
    IntegralRecordMapper integralRecordMapper;
    @Autowired
    PointsMallProductsMapper pointsMallProductsMapper;
    @Autowired
    PointsExchangeRecordMapper pointsExchangeRecordMapper;
    @Autowired
    MallProductsDetailMapper mallProductsDetailMapper;
    @Autowired MallAreaMapper mallAreaMapper;
    @Autowired PaymentsMapper paymentsMapper;

    @Override
    public int integralUserinsert(IntegralUser record){
        return integralUserMapper.insert(record);
    };

    /**
     * 增加积分
     * @param integral
     * @param userId
     * @return
     */
    @Override
    public int integralIncrease(Integer integral,Long userId){
        IntegralUser integralUser=new IntegralUser();
        integralUser.setUserId(userId);
        IntegralUser user=integralUserMapper.findUserIntegral(userId);
        Integer oldIntegral = user.getIntegral();
        int integrals=oldIntegral+integral;
        logger.info("更新后积分:"+integrals);
        integralUser.setIntegral(integrals);
        IntegralRecord integralRecord=new IntegralRecord();
        integralRecord.setRecord("+"+integral);
        integralRecord.setUserId(userId);
        Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime());
        integralRecord.setTime(timestamp);
        integralRecordMapper.insert(integralRecord);
        return integralUserMapper.integralChange(integralUser);
    };

    /**
     * 减少积分
     * @param integral
     * @param userId
     * @return
     */
    @Override
    public int integralReduction(Integer integral,Long userId){
        IntegralUser integralUser=new IntegralUser();
        integralUser.setUserId(userId);
        integralUser.setIntegral(integral);
        IntegralUser user=integralUserMapper.findUserIntegral(userId);
        Integer oldIntegral = user.getIntegral();
        integralUser.setIntegral(oldIntegral-integral);
        IntegralRecord integralRecord=new IntegralRecord();
        integralRecord.setRecord("-"+integral);
        integralRecord.setUserId(userId);
        Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime());
        integralRecord.setTime(timestamp);
        integralRecordMapper.insert(integralRecord);
        return integralUserMapper.integralChange(integralUser);
    };

    /**
     * 通过用户Id查找用户积分
     * @param userId
     * @return
     */
    @Override
    public IntegralUser findUserIntegral(Long userId){
        return integralUserMapper.findUserIntegral(userId);
    };

    /**
     * 通过积分商品Id获取积分商品信息
     * @param pointsMallProductsId
     * @return
     */
    @Override
    public PointsMallProducts findMallProducts(Long pointsMallProductsId){
        return pointsMallProductsMapper.findMallProducts(pointsMallProductsId);
    };

    /**
     * 添加商品兑换记录
     * @param record
     * @return
     */
    public Long exchangeRecordinsert(PointsExchangeRecord record){
        pointsExchangeRecordMapper.insert(record);
        Long recordId=record.getPointsExchangeRecordId();
        return recordId;
    };

    /**
     * 通过Id查询商品规格信息
     * @param productDetailId
     * @return
     */
    @Override
    public MallProductsDetail findProductForDetail(Long productDetailId){
        return mallProductsDetailMapper.findProductForDetail(productDetailId);
    };

    /**
     * 查询所有商城分类
     * @return
     */
    @Override
    public List<MallArea> mallareaselectAll(){
        return mallAreaMapper.selectAll();
    };

    /**
     * 通过分区Id查看商品
     * @param respectiveArea
     * @return
     */
    @Override
    public Page<PointsMallProducts> findProductForArea(Long respectiveArea){
        return pointsMallProductsMapper.findProductForArea(respectiveArea);
    };

    /**
     * 查询兑换记录
     * @param userId
     * @return
     */
    @Override
    public Page<PointsExchangeRecord> findExchangeRecord(Long userId){
        return pointsExchangeRecordMapper.findExchangeRecord(userId);
    };

    /**
     * 通过商品Id查询信息
     * @param productId
     * @return
     */
    @Override
    public PointsMallProducts findProductForProductId(Long productId){
        return mallProductsDetailMapper.findProductForProductId(productId);
    };

    /**
     * 通过Id查询兑换记录
     * @param pointsExchangeRecordId
     * @return
     */
    @Override
    public PointsExchangeRecord findExchangeRecordForId(Long pointsExchangeRecordId){
        return pointsExchangeRecordMapper.findExchangeRecordForId(pointsExchangeRecordId);
    };

    /**
     * 添加支付记录
     * @param payments
     * @return
     */
    public int paymentsinsert(Payments payments){
        return paymentsMapper.insert(payments);
    };
}
