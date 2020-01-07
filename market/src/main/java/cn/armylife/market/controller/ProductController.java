package cn.armylife.market.controller;


import cn.armylife.common.domain.*;
import cn.armylife.common.util.NumberID;
import cn.armylife.market.config.RedisDao;
import cn.armylife.market.domain.RankShop;
import cn.armylife.market.feign.MemberService;
import cn.armylife.market.service.OrderService;
import cn.armylife.market.service.ProductService;
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

    @Autowired
    MemberService memberService;


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
        Long shopId=member.getMemberId();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd hh/mm/ss");
        int num=0;
        try{
            List<ShopTag> shopTags=productService.selectShopTag(shopId);
            num=shopTags.size()+1;
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        Date date=new Date();
        String creatTime=sdf.format(date);
        shopTag.setTagId(num);
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
        logger.info("商家信息"+member.toString());
        Long shopId=member.getMemberId();
        return productService.selectShopTag(shopId);
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
        if (!member.getAlid().equals("REST0202")){
            product.setProductBoxfee(new BigDecimal(0));
        }
        product.setStatus(0);
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
            File pic = new File("/home/armylife/shopAvatar/public_html/" + newName + exeName);
//            File pic = new File("E:\\Temp\\" + newName + exeName);
            // 保存图片到本地磁盘
            file.transferTo(pic);
            product.setProductAvator("http://pic.armylife.cn/"+newName + exeName);
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
        logger.info("用户信息",member.toString());
//        int num=0;
//        try {
//            num=(int)redisTemplate.opsForValue().get("NumId-"+member.getMemberId())+1;
//        }catch (NullPointerException e){
//            e.printStackTrace();
//            num=1;
//        }
        ListOperations<String, Object> lo = redisTemplate.opsForList();
//        redisTemplate.opsForValue().set("NumId-"+member.getMemberId(),num);;
        lo.leftPush("userId-"+member.getMemberId(),orderDetail);
        return 1;
    }

    @RequestMapping("/ReExpressCart")
    @ResponseBody
    public int ReExpresspingCart(int num,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        redisTemplate.opsForList().remove("userId-"+member.getMemberId(),0,num);
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
        List<Product> productList=(List<Product>) lo.index("userId-"+member.getMemberId()+"s"+shopId, 0);
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
            if (day==null){
                day="0";
            }
            String month=orderService.rankMonthPayForShop(shopId);
            if (month==null){
                month="0";
            }
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

    /**
     * 去理发
     * @param shopOrder
     * @param request
     * @return
     */
    @RequestMapping("goVipHairOrder")
    @ResponseBody
    public int goVipHairOrder(ShopOrder shopOrder,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String creatime=sdf.format(date);
        shopOrder.setOrdersStatus("3");
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

    /**
     * 添加会员
     * @param request
     * @param out_trade_no
     * @param memberId
     * @return
     */
    @RequestMapping("plusVipHairOrder")
    @ResponseBody
    public int plusVipHairOrder(HttpServletRequest request,String out_trade_no,Long memberId){
        HttpSession session=request.getSession();
        Member member=memberService.selectMemberforId(memberId);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String creatime=sdf.format(date);
        logger.info("支付订单:"+out_trade_no);
        ShopOrder order=orderService.selectOrder(Long.valueOf(out_trade_no));
        Hairvip hairvip=productService.selectHairvip(member.getMemberId());
        if (hairvip==null){
            Hairvip hairvip1=new Hairvip();
            hairvip1.setVipId(order.getStuId());
            hairvip1.setHairvipNum("11");
            hairvip1.setHairvipName(order.getMemberName());
            hairvip1.setHaivipAddress(order.getOrdersAddress());
            hairvip1.setCreatTime(creatime);
            hairvip1.setHairvipPhone(order.getUserPhone());
            productService.hairInsert(hairvip1);
            ShopOrder shopOrder=new ShopOrder();
            shopOrder.setOrdersStatus("6");
            shopOrder.setEndTime(new Date(creatime));
            shopOrder.setOrdersId(Long.valueOf(out_trade_no));
            return orderService.endOrders(shopOrder);
        }else {
        String num=hairvip.getHairvipNum();
        int oldnum=Integer.valueOf(num);
        int newnum=oldnum+11;
        Hairvip vip=new Hairvip();
        vip.setHairvipNum(String.valueOf(newnum));
        vip.setVipId(member.getMemberId());
        int msg=productService.updateHair(vip);
        if (msg==0){
            productService.updateHair(vip);
        }}
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setOrdersStatus("6");
        shopOrder.setOrdersId(Long.valueOf(out_trade_no));
        return orderService.endOrders(shopOrder);
    }

    /**
     * 查询新理发店会员
     * @param request
     * @return
     */
    @RequestMapping("selecthairVip")
    @ResponseBody
    public Hairvip selecthairVip(HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        Hairvip uservip=productService.selectHairvip(member.getMemberId());
        return uservip;
    }

    /**
     * 查询老理发店会员
     * @param request
     * @return
     */
    @RequestMapping("selectVip")
    @ResponseBody
    public int selectVip(String userName,String groupNumber,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        return productService.FindVip(userName,groupNumber,member.getMemberId());
    }

    /**
     * 得到理发店排队人数(通过订单Id)
     * @return
     */
    @RequestMapping("getShopPeople")
    @ResponseBody
    public int getShopPeople(Long orderId){
        int num=orderService.getShopPeople(orderId);
        return num+1;
    };


}
