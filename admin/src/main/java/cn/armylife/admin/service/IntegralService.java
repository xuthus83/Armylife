package cn.armylife.admin.service;

import cn.armylife.admin.domain.MallProductsDetail;
import cn.armylife.admin.domain.MallProductsPicture;
import cn.armylife.admin.domain.PointsMallProducts;


public interface IntegralService {

    /**
     * 积分商城商品添加
     * @param record
     * @return
     */
    int Productinsert(PointsMallProducts record, MallProductsDetail mallProductsDetail, MallProductsPicture mallProductsPicture);
}
