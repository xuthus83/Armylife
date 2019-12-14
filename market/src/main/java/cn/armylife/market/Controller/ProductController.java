package cn.armylife.market.Controller;


import cn.armylife.common.Domain.*;
import cn.armylife.common.Util.NumberID;
import cn.armylife.market.Config.RedisDao;
import cn.armylife.market.Domain.RankShop;
import cn.armylife.market.Service.OrderService;
import cn.armylife.market.Service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ProductController {

    Logger logger= LoggerFactory.getLogger(ProductController.class);

    @Autowired private ProductService productService;
    @Autowired
    RedisDao redisDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    OrderService orderService;


    @Value("${server.port}")
    int port;

    /**
     * 查询商铺商品
     * @param request
     * @return
     */
    @RequestMapping("selectProductForShop")
    @ResponseBody
    public List<Product> selectProductForShop(HttpServletRequest request){
        Product product=new Product();
        HttpSession session=request.getSession();
        Member member=(Member) session.getAttribute("Shop");
        product.setShopId(member.getMemberId());
        if (member==null){
            logger.info("当前未登录帐号,请前往登录页面!");
            return null;
        }
        return productService.selectProductForShop(product);
    }

    /**
     * 下架商品
     * @param product
     * @return
     */
    @RequestMapping("offShelves")
    @ResponseBody
    public int offShelves(Product product, HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member) session.getAttribute("Shop");
        if (member==null){
            logger.info("当前未登录帐号,请前往登录页面!");
            return 0;
        }
        return productService.offShelves(product.getProductId());
    }

    /**
     * 查询商品信息
     * @param productId
     * @return
     */
    @RequestMapping("selectProductNews")
    @ResponseBody
    public Map<Product, String> selectProductNews(List<String> productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("Shop");
        if (member == null) {
            logger.info("当前未登录帐号,请前往登录页面!");
            return null;
        }
        Map<Product, String> productMap = new HashMap<>();
        for (int i = 0; i < productId.size(); i++) {
            String[] productIds = productId.get(i).split(",");
            Product products = productService.selectProductNews(productIds[1]);
            productMap.put(products, productIds[2]);
        }
        return productMap;
    }
    /**
     * 通过分类查询得到全部商品
     * @param request
     * @return
     */
    @RequestMapping("selectShopProduct")
    @ResponseBody
    public List<ShopTag> selectShopProduct(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        return productService.selectShopProduct(member.getMemberId());

    };



    /**
     * 添加商铺商品分类
     * @param shopTag
     * @param request
     * @return
     */
    @RequestMapping("/AddProductTag")
    @ResponseBody
    public int AddProductTag(ShopTag shopTag,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd hh/mm/ss");
        Date date=new Date();
        String creatTime=sdf.format(date);
        shopTag.setCreatTime(creatTime);
        shopTag.setShopId(member.getMemberId());
        return productService.TagInsert(shopTag);
    }

    /**
     * 查询店铺商品分类
     * @param request
     * @return
     */
    @RequestMapping("selectShopTag")
    @ResponseBody
    public List<ShopTag> selectShopTag(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        logger.info(member.toString());
        return productService.selectShopTag(member.getMemberId());
    };

    /**
     * 上架商品
     * @param product
     * @return
     */
    @RequestMapping("grounding")
    @ResponseBody
    public int grounding(MultipartFile file, Product product, HttpServletRequest request) throws IOException {
        HttpSession session=request.getSession();
        Member member=(Member) session.getAttribute("Shop");
        product.setShopId(member.getMemberId());
        if (member==null){
            logger.info("当前未登录帐号,请前往登录页面!");
            return 2;
        }
        String newName = null;
        String exeName = null;
        // 判断文件是否为空
        if (!file.isEmpty()) {
            logger.info("123");
            // 图片原始名字
            String oldName = file.getOriginalFilename();
            // 图片新名字
            newName = UUID.randomUUID().toString();
            // 后缀名
            // exeName = oldName.substring(oldName.indexOf("."));
            exeName=".png";
//            File pic = new File("/root/armylife/shopAvatar/" + newName + exeName);
            File pic = new File("E:\\Temp\\" + newName + exeName);
            // 保存图片到本地磁盘
            file.transferTo(pic);
            product.setProductAvator("http://api.xuthus83.cn:6081/img/"+newName + exeName);
        }

        return productService.insert(product);
    }

    /**
     * 更新商品
     * @param product
     * @return
     */
    @RequestMapping("updateProducts")
    @ResponseBody
    public int updateProducts(Product product, HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member) session.getAttribute("Shop");
        if (member==null){
            logger.info("当前未登录帐号,请前往登录页面!");
            return 2;
        }
        return productService.updateProducts(product);
    };

//    @RequestMapping("AddShoppingCarts")
//    @ResponseBody


    /**
     * 添加购物车
     * @param request
     * @return
     */
    @RequestMapping("/AddShoppingCart")
    @ResponseBody
    public int AddShoppingCart(@RequestParam(value = "productIds[]") String[] productIds, String shopId, String total, HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        for (int i=0;i<productIds.length;i++){
            logger.info("product"+productIds[i]);//.get(i));
        }
        return productService.AddShoppingCarts(productIds,shopId,total,member.getMemberId());
    }

    /**
     * 添加快递购物车
     * @param request
     * @return
     */
    @RequestMapping("/AddExpressCart")
    @ResponseBody
    public int AddExpresspingCart(OrderDetail orderDetail,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        ListOperations<String, OrderDetail> lo = redisTemplate.opsForList();
        lo.leftPush("userId-"+member.getMemberId(),orderDetail);
        return 1;
    }

    /**
     * 查询快递里的商品
     * @param request
     * @return
     */
    @RequestMapping("SelectExpressCart")
    @ResponseBody
    public List<OrderDetail> SelectExpressCart(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        List<OrderDetail> expressList=new ArrayList<>();
        ListOperations<String, OrderDetail> lo = redisTemplate.opsForList();
        for (int i=0;i<lo.size("userId-"+member.getMemberId());i++){
            expressList.add(lo.index("userId-"+member.getMemberId(), i));
        }
        return expressList;
    }

    /**
     * 查询购物车里的商品
     * @param request
     * @return
     */
    @RequestMapping("SelectShoppingCart")
    @ResponseBody
    public List<Product> SelectShoppingCart(Long shopId,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        ListOperations<String, Object> lo = redisTemplate.opsForList();
        List<Product> productList=(List<Product>) lo.index("userId-"+member.getMemberId()+shopId, 0);
        return productList;
    }

    /**
     * 统计
     * @param request
     * @return
     */
    @RequestMapping("rankForProduct")
    @ResponseBody
    public RankShop rankForProduct(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        Long shopId=member.getMemberId();
        Map<String,String> map=new HashMap<>();
        RankShop rankShop=new RankShop();
        try{
            List<Product> products=productService.rankForProduct(shopId);
            String day=orderService.rankDayPayForShop(shopId);
            String month=orderService.rankMonthPayForShop(shopId);
            rankShop.setRankDay(day);
            rankShop.setRankMonth(month);
            rankShop.setProduct(products);
        }catch (NullPointerException e){
            logger.info("空指针异常:"+e);
        }

        return rankShop;
    };


    /**
     * 创建理发店会员(未生效,支付)
     * @param request
     * @param shopOrder
     * @return
     */
    @RequestMapping("JoinBarberMember")
    @ResponseBody
        public Long hairVip(HttpServletRequest request, ShopOrder shopOrder){
            HttpSession session=request.getSession();
            Member member=(Member) session.getAttribute("Student");
            if(productService.checkHairvip(member.getMemberId())){
                return 0L;
            }
            //创建未生效的理发店会员信息
            Hairvip hairvip=new Hairvip();
            hairvip.setVipId(member.getMemberId());
            hairvip.setHairvipNum("11");
            hairvip.setIs(0);
            hairvip.setHairvipName(member.getMemberName());
            hairvip.setHairvipPhone(shopOrder.getUserPhone());
            productService.hairInsert(hairvip);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
            Date date=new Date();
            String creatime=sdf.format(date);
            shopOrder.setCreatTime(creatime);
            shopOrder.setOrdersStatus("0");
            Long int1= NumberID.nextId(port);
            shopOrder.setOrdersId(int1);
            shopOrder.setStuId(member.getMemberId());
            BigDecimal total=new BigDecimal(100);
            shopOrder.setOrderTotal(total);
            logger.info("SopOrder:"+shopOrder);
            orderService.VIPinsert(shopOrder);
            return int1;
    }

    @RequestMapping("goVipHairOrder")
    @ResponseBody
    public int goVipHairOrder(ShopOrder shopOrder,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String creatime=sdf.format(date);
        shopOrder.setOrdersStatus("4");
        Long int1= NumberID.nextId(port);
        shopOrder.setOrdersId(int1);
        shopOrder.setStuId(member.getMemberId());
        BigDecimal total=new BigDecimal(0);
        shopOrder.setOrderTotal(total);
        shopOrder.setCreatTime(creatime);
        Hairvip hairvip=productService.selectHairvip(member.getMemberId());
        String num=hairvip.getHairvipNum();
        Hairvip vip=new Hairvip();
        int oldnum=Integer.valueOf(num);
        int newnum = oldnum-1;
        vip.setHairvipNum(String.valueOf(newnum));
        vip.setVipId(member.getMemberId());
        productService.updateHair(vip);
        return orderService.VIPinsert(shopOrder);
    }

    @RequestMapping("plusVipHairOrder")
    @ResponseBody
    public int plusVipHairOrder(HttpServletRequest request,String out_trade_no){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String creatime=sdf.format(date);
        Hairvip hairvip=productService.selectHairvip(member.getMemberId());
        String num=hairvip.getHairvipNum();
        Hairvip vip=new Hairvip();
        vip.setHairvipNum(num+"11");
        vip.setVipId(member.getMemberId());
        return productService.updateHair(vip);
    }

    @RequestMapping("selecthairVip")
    @ResponseBody
    public Hairvip selecthairVip(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        Hairvip uservip=productService.selectHairvip(member.getMemberId());
        return uservip;
    }
}
