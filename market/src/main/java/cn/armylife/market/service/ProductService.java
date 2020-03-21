package cn.armylife.market.service;

import cn.armylife.common.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface ProductService {


    /**
     * 查询全部商品
     * @param product
     * @return
     */
    List<Product> selectProductForShop(Product product);

    /**
     * 商品下架
     * @param productId
     * @return
     */
    int offShelves(Long productId);

    /**
     * 商品信息
     * @param product
     * @return
     */
    Product selectProductNews(String product);

    /**
     * 上架商品
     * @param record
     * @return
     */
    int insert(Product record);

    /**
     * 更新商品信息
     * @param product
     * @return
     */
    int updateProducts(Product product);

    /**
     * 添加商铺分类
     * @param record
     * @return
     */
    int TagInsert(ShopTag record);

    /**
     * 查询店铺商品分类
     * @param shopId
     * @return
     */
    List<ShopTag> selectShopTag(Long shopId);

    /**
     * 添加商品详细信息
     * @param record
     * @return
     */
    int detailinsert(ProductDetail record);

    /**
     * 查询店铺所有商品
     * @param shopId
     * @return
     */
    List<ShopTag> selectShopProduct(Long shopId);

    /**
     * 当日商品统计
     * @param shopId
     * @return
     */
    List<Product> rankForProduct(Long shopId);

    /**
     * 添加商品至购物车
     * @param productIds
     * @param shopId
     * @param total
     * @param member
     * @return
     */
    int AddShoppingCarts(String[] productIds, String shopId, String total, Long member);



    /**
     * 理发店会员加入
     * @param record
     * @return
     */
    int hairInsert(Hairvip record);

    /**
     * 是否有会员信息
     * @param memberId
     * @return
     */
    boolean checkHairvip(long memberId);

    Hairvip selectHairvip(long memberId);


    /**
     * 续费会员
     * @param hairvip
     * @return
     */
    int updateHair(Hairvip hairvip);

    int orderdetailinsert(OrderDetail record);

    /**
     * 查询认领会员
     * @param userName
     * @param groupNumber
     * @return
     */
    int FindVip(String userName,String groupNumber,Long memberId);

    /**
     * 删除商户分类
     * @param shopTagId
     * @return
     */
    int removeTag(Long shopTagId,Long shopId);

    /**
     * 更新shop_tag的tag_name名
     * @param shopTag
     * @return
     */
    int updateTagName(ShopTag shopTag);

    /**
     * 查询商品信息
     * @param productId
     * @return
     */
    Product getProduct(Long productId);

}
