package cn.armylife.admin.controller;


import cn.armylife.admin.domain.MallProductsPicture;
import cn.armylife.admin.domain.PointsMallProducts;
import cn.armylife.admin.service.IntegralService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@Controller
public class IntegralMallController {

    Logger logger= LoggerFactory.getLogger(IntegralMallController.class);

    @Autowired
    IntegralService integralService;

    /**
     * 积分商城商品添加
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping("picture")
    @ResponseBody
    public int upload(MultipartFile[] file,Long productId,HttpServletRequest request) {

        String newName = null;
        String exeName = null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        Date date=new Date();
        if(file!=null){

            String fileName=null;

            try {
                for (int i = 0; i < file.length; i++) {

                    if (!file[i].isEmpty()) {

                        logger.info("图片不为空");
                        // 图片原始名字
                        String oldName = file[i].getOriginalFilename();
                        // 图片新名字
                        newName = UUID.randomUUID().toString();
                        // 后缀名
                        // exeName = oldName.substring(oldName.indexOf("."));
                        exeName=".png";
//                        File pic = new File("/home/armylife/shopAvatar/public_html/Mall/" + newName + exeName);
                        File pic = new File("E:\\work\\armylife\\shopavatar/" + newName + exeName);
                        // 保存图片到本地磁盘
                        file[i].transferTo(pic);
                        MallProductsPicture mallProductsPicture=new MallProductsPicture();
                        mallProductsPicture.setPictureUrl("http://pic.armylife.cn/Mall/" +newName + exeName);
                        mallProductsPicture.setProductId(productId);
                        integralService.pictureInsert(mallProductsPicture);
                        return 1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return 0;
        }
        return 0;
    };

    /**
     * 添加商品
     * @param pointsMallProducts
     * @return
     */
    @RequestMapping("addProduct")
    @ResponseBody
    public Long addProduct(PointsMallProducts pointsMallProducts){
        logger.info("商品详情:"+pointsMallProducts.toString());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        Date date=new Date();
        pointsMallProducts.setAddingTime(date);
        Long id=integralService.productinsert(pointsMallProducts);
        return id;
    }

    /**
     * 查询所有积分商品
     * @param pageNum
     * @return
     */
    @RequestMapping("productSelectAll")
    @ResponseBody
    public PageInfo<PointsMallProducts> productSelectAll(int pageNum){
        PageHelper.startPage(pageNum,10);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        PageInfo<PointsMallProducts> pageInfo=new PageInfo<>(integralService.productSelectAll());
        return pageInfo;
    }

    /**
     * 更新商品数据
     * @param pointsMallProducts
     * @return
     */
    @RequestMapping("updateProduct")
    @ResponseBody
    public int updateProduct(PointsMallProducts pointsMallProducts){
        return integralService.updateProduct(pointsMallProducts);
    };

    /**
     * 通过ID查询商品
     * @param pointsMallProductsId
     * @return
     */
    @RequestMapping("selectForId")
    @ResponseBody
    public PointsMallProducts selectForId(Long pointsMallProductsId){
        return integralService.selectForId(pointsMallProductsId);
    };


}
