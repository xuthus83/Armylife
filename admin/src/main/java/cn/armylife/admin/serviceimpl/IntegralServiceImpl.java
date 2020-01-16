package cn.armylife.admin.serviceimpl;

import cn.armylife.admin.domain.Admin;
import cn.armylife.admin.domain.MallProductsPicture;
import cn.armylife.admin.domain.PointsMallProducts;
import cn.armylife.admin.mapper.AdminMapper;
import cn.armylife.admin.mapper.MallProductsDetailMapper;
import cn.armylife.admin.mapper.MallProductsPictureMapper;
import cn.armylife.admin.mapper.PointsMallProductsMapper;
import cn.armylife.admin.service.IntegralService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegralServiceImpl implements IntegralService {

    @Autowired
    PointsMallProductsMapper pointsMallProductsMapper;
    @Autowired
    MallProductsPictureMapper mallProductsPictureMapper;
    @Autowired
    MallProductsDetailMapper mallProductsDetail;
    @Autowired
    AdminMapper adminMapper;


    /**
     * 积分商城商品添加
     * @param pointsMallProducts
     * @return
     */
    @Override
    public Long productinsert(PointsMallProducts pointsMallProducts){
       pointsMallProductsMapper.insert(pointsMallProducts);
       return pointsMallProducts.getPointsMallProductsId();
    };

    /**
     * 积分商城商品添加
     * @param
     * @return
     */
    @Override
    public int pictureInsert(MallProductsPicture mallProductsPicture){
        return mallProductsPictureMapper.insert(mallProductsPicture);
    };

    /**
     * 查询全部积分商品
     * @return
     */
    @Override
    public Page<PointsMallProducts> productSelectAll(){
        return pointsMallProductsMapper.selectAll();
    };

    /**
     * 更新商品数据
     * @param pointsMallProducts
     * @return
     */
    @Override
    public int updateProduct(PointsMallProducts pointsMallProducts){
        return pointsMallProductsMapper.updateProduct(pointsMallProducts);
    };

    /**
     * 通过ID查询商品
     * @param pointsMallProductsId
     * @return
     */
    @Override
    public PointsMallProducts selectForId(Long pointsMallProductsId){
        return pointsMallProductsMapper.selectForId(pointsMallProductsId);
    };

}
