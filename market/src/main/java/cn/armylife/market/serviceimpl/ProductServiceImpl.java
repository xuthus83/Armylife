package cn.armylife.market.serviceimpl;

import cn.armylife.common.domain.*;
import cn.armylife.market.mapper.*;
import cn.armylife.market.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {


    Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired private ProductMapper productMapper;
    @Autowired private ShopTagMapper shopTagMapper;
    @Autowired private ProductDetailMapper productDetailMapper;
    @Autowired private HairvipMapper hairvipMapper;
    @Autowired private VipMapper vipMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;

    /**
     * 查询全部商品
     * @param product
     * @return
     */
    @Override
    public List<Product> selectProductForShop(Product product){
        return productMapper.selectProductForShop(product);
    };

    /**
     * 商品下架
     * @param productId
     * @return
     */
    @Override
    public int offShelves(Long productId){
        return productMapper.offShelves(productId);
    };

    /**
     * 商品信息
     * @param product
     * @return
     */
    @Override
    public Product selectProductNews(String product){
        return productMapper.selectProductNews(product);
    };


    /**
     * 上架商品
     * @param record
     * @return
     */
    @Override
    public int insert(Product record){
        return productMapper.insert(record);
    };


    @Override
    public int updateProducts(Product product){
        return productMapper.updateProducts(product);
    };

    /**
     * 添加商铺分类
     * @param record
     * @return
     */
    public int TagInsert(ShopTag record){
        return shopTagMapper.insert(record);
    };

    /**
     * 查询店铺商品分类
     * @param shopId
     * @return
     */
    public List<ShopTag> selectShopTag(Long shopId){
        return shopTagMapper.FindShopTag(shopId);
    };

    /**
     * 添加商品详细信息
     * @param record
     * @return
     */
    public int detailinsert(ProductDetail record){
        return productDetailMapper.insert(record);
    };

    /**
     * 查询店铺所有商品
     * @param shopId
     * @return
     */
    public List<ShopTag> selectShopProduct(Long shopId){
        return shopTagMapper.selectShopProduct(shopId);
    };

    /**
     * 当日商品统计
     * @param shopId
     * @return
     */
    public List<Product> rankForProduct(Long shopId){
        List<Product> products=productMapper.rankForProduct(shopId);
        return products;
    };

    /**
     *
     * @param productIds
     * @param shopId
     * @param total
     * @param member
     * @return
     */
    public int AddShoppingCarts(String[] productIds, String shopId, String total, Long member){
        Product product=new Product();
        List<Product> lp=new ArrayList<>();
        for (int i=0;i<productIds.length;i++) {
            String[] str2 = productIds[i].split("\\*");
            product = productMapper.selectProductNews(str2[0]);
            product.setOrderNum(str2[1]);
            logger.info(product.toString());
            lp.add(product);
        }
        ListOperations<String, Object> lo = redisTemplate.opsForList();
        lo.leftPop("userId-" +member+"s"+shopId);
        lo.rightPush("userId-" +member+"s"+shopId,lp);
        return 1;
    }



    /**
     * 理发店会员加入
     * @param record
     * @return
     */
    public int hairInsert(Hairvip record){
        return hairvipMapper.insert(record);
    };

    /**
     * 是否有会员信息
     * @param memberId
     * @return
     */
    public boolean checkHairvip(long memberId){
        Hairvip hairvip= hairvipMapper.selectHairvip(memberId);
        if (hairvip==null){
            return false;
        }
        return true;
    };

    public Hairvip selectHairvip(long memberId){
       return hairvipMapper.selectHairvip(memberId);
    };



    /**
     * 续费会员
     * @param hairvip
     * @return
     */
    public int updateHair(Hairvip hairvip){
        return hairvipMapper.updateHair(hairvip);
    };

    public int orderdetailinsert(OrderDetail record){
        return orderDetailMapper.insert(record);
    };

    /**
     * 查询认领会员
     * @param userName
     * @param groupNumber
     * @return
     */
    public int FindVip(String userName,String groupNumber,Long memberId){
        Vip vip=vipMapper.FindVip(userName,groupNumber);
        if (vip!=null){
            vipMapper.removeVip(vip.getVipId());
            Hairvip hairvip=new Hairvip();
            hairvip.setHairvipName(vip.getUserName());
            int number=Integer.valueOf(vip.getNumber());
            int newnum=11-number;
            hairvip.setHairvipNum(String.valueOf(newnum));
            hairvip.setHaivipAddress(vip.getGroupNumber());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time=simpleDateFormat.format(new Date());
            hairvip.setCreatTime(time);
            hairvip.setIs(1);
            hairvip.setVipId(memberId);
           return hairvipMapper.insert(hairvip);
        }else {
            return 0;
        }
    };

    /**
     * 删除商户分类
     * @param shopTagId
     * @return
     */
    public int removeTag(Long shopTagId,Long shopId){
        List<ShopTag> shopTag=shopTagMapper.FindShopTag(shopId);
        for (int i=0;i<shopTag.size();i++){
            ShopTag tag=shopTag.get(i);
            int tagId=0;
            if (shopTagId==tag.getShopTagId()){
                tagId=tag.getTagId();
            }
            if (tag.getTagId()>tagId){
                int newTagId=tag.getTagId();
                shopTagMapper.updateTag(shopTagId,newTagId);
            }
        }
        return shopTagMapper.removeTag(shopTagId);
    };

    /**
     * 更新shop_tag的tag_name名
     * @param shopTag
     * @return
     */
    public int updateTagName(ShopTag shopTag){
        return shopTagMapper.updateTagName(shopTag);
    };

    /**
     * 查询商品信息
     * @param productId
     * @return
     */
    public Product getProduct(Long productId){
        return productMapper.getProduct(productId);
    };
}
