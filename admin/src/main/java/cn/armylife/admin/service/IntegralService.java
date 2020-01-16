package cn.armylife.admin.service;

import cn.armylife.admin.domain.Admin;
import cn.armylife.admin.domain.MallProductsPicture;
import cn.armylife.admin.domain.PointsMallProducts;
import com.github.pagehelper.Page;


public interface IntegralService {

    /**
     * 积分商城商品添加
     * @param
     * @return
     */
    Long productinsert(PointsMallProducts pointsMallProducts);

    /**
     * 积分商城商品添加
     * @param
     * @return
     */
    int pictureInsert(MallProductsPicture mallProductsPicture);

    /**
     * 查询全部积分商品
     * @return
     */
    Page<PointsMallProducts> productSelectAll();

    /**
     * 更新商品数据
     * @param pointsMallProducts
     * @return
     */
    int updateProduct(PointsMallProducts pointsMallProducts);

    /**
     * 通过ID查询商品
     * @param pointsMallProductsId
     * @return
     */
    PointsMallProducts selectForId(Long pointsMallProductsId);

}
