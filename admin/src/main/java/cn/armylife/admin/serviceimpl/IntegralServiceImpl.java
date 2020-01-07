package cn.armylife.admin.serviceimpl;

import cn.armylife.admin.domain.MallProductsPicture;
import cn.armylife.admin.domain.PointsMallProducts;
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


    /**
     * 积分商城商品添加
     * @param mallProductsPicture
     * @return
     */
    @Override
    public int Productinsert(MallProductsPicture mallProductsPicture){
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


}
