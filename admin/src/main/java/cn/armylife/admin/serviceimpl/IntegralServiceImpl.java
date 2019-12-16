package cn.armylife.admin.serviceimpl;

import cn.armylife.admin.domain.MallProductsDetail;
import cn.armylife.admin.domain.MallProductsPicture;
import cn.armylife.admin.domain.PointsMallProducts;
import cn.armylife.admin.mapper.MallProductsDetailMapper;
import cn.armylife.admin.mapper.MallProductsPictureMapper;
import cn.armylife.admin.mapper.PointsMallProductsMapper;
import cn.armylife.admin.service.IntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegralServiceImpl implements IntegralService {

    @Autowired
    PointsMallProductsMapper pointsMallProductsMapper;
    @Autowired
    MallProductsPictureMapper mallProductsPicture;
    @Autowired
    MallProductsDetailMapper mallProductsDetail;


    /**
     * 积分商城商品添加
     * @param record
     * @return
     */
    @Override
    public int Productinsert(PointsMallProducts record, MallProductsDetail mallProductsDetail, MallProductsPicture mallProductsPicture){

       return pointsMallProductsMapper.insert(record);
    };


}
