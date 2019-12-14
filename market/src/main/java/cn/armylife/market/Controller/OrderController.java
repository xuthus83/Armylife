package cn.armylife.market.Controller;

import cn.armylife.common.Domain.*;
import cn.armylife.market.Feign.MemberService;
import cn.armylife.market.Mapper.DeliveryOrderMapper;
import cn.armylife.market.Service.OrderService;
import cn.armylife.market.Service.ProductService;
import cn.armylife.market.Util.NumberID;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@RestController
public class OrderController {
    Logger logger=Logger.getLogger("OrderController.class");

    @Autowired
    MemberService memberService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    DeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    RedisTemplate redisTemplate;


    @Value("${server.port}")
    int port;

    /**
     * 添加订单
     * @param shopOrder
     * @return
     */
    @RequestMapping("creatShopOrder")
    @ResponseBody
    public Long creatShopOrder(ShopOrder shopOrder,String orderExpress, HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String creatime=sdf.format(date);
        shopOrder.setCreatTime(creatime);
        shopOrder.setOrdersStatus("0");
        Long int1= NumberID.nextId(port);
        shopOrder.setOrdersId(int1);
        shopOrder.setStuId(member.getMemberId());
        logger.info("SopOrder:"+shopOrder);
        List<Product> productList=new ArrayList<>();
        List<OrderDetail> expressList=new ArrayList<>();
        ListOperations<String, Object> lo = redisTemplate.opsForList();
        productList=(List<Product>) lo.index("userId-"+member.getMemberId()+shopOrder.getShopId(), 0);
        logger.info("orderExpress:"+orderExpress);
        try {
            if (orderExpress.equals("1")){
            ListOperations<String, OrderDetail> ll = redisTemplate.opsForList();
            for (int i=0;i<ll.size("userId-"+member.getMemberId());i++){
                shopOrder.setIsdelivery(1);
                expressList.add(ll.index("userId-"+member.getMemberId(), i));
            }
            for (int i=0;i<expressList.size();i++){
                OrderDetail orderDetail=expressList.get(i);
                orderDetail.setOrderId(int1);
                orderService.orderdatailinsert(orderDetail);
            }
        }else {
                logger.info("不是快递");
            orderExpress="0";
        }
    }catch (NullPointerException e){
            logger.info(""+e);
            orderExpress="0";
        }
        int msg =orderService.insert(shopOrder,productList,orderExpress);
        if (msg==0){
            return 0l;
        }
        lo.rightPop("userId-"+member.getMemberId()+shopOrder.getShopId());
        lo.rightPop("userId-"+member.getMemberId());
        return int1;
    }

    /**
     * 查询学员与商铺的订单
     * @param shopOrder
     * @param request
     * @return
     */
    @RequestMapping("selectAllForMember")
    @ResponseBody
    public PageInfo<ShopOrder> selectAllForMember(ShopOrder shopOrder, int pageNum,int type, HttpServletRequest request){
        HttpSession session=request.getSession();
        PageHelper.startPage(pageNum, 10);
        Member member=new Member();
        switch (type){
            case 1:
                member=(Member) session.getAttribute("Student");
                shopOrder.setStuId(member.getMemberId());
                break;
            case 2:
                member=(Member) session.getAttribute("Shop");
                shopOrder.setShopId(member.getMemberId());
                break;
        }
        if (member==null){
            return null;
        }

        PageInfo<ShopOrder> pageInfo = new PageInfo<>(orderService.selectAllForMember(shopOrder));
        if (pageInfo.getPageNum()==1&pageNum>1){
            return null;
        }
        return pageInfo;
    };

    /**
     * 查询跑腿订单
     * @param deliveryOrder
     * @param request
     * @return
     */
    @RequestMapping("selectAlldeliveryForMember")
    @ResponseBody
    public PageInfo<DeliveryOrder> selectAlldeliveryForMember(DeliveryOrder deliveryOrder,int pageNum,HttpServletRequest request){
        HttpSession session=request.getSession();
        PageHelper.startPage(pageNum, 10);
        Member member=(Member)session.getAttribute("Delivery");
        if (member==null||!member.getMemberType().equals("2")){
            logger.info("用户未登录,阻止进行");
            return null;
        }
        switch (member.getMemberType()){
            case "1":
                deliveryOrder.setDeliveryUserId(member.getMemberId());
                break;
            case "2":
                deliveryOrder.setDeliveryUserId(member.getMemberId());
                break;
            default:
                return null;
        }
        PageInfo<DeliveryOrder> pageInfo = new PageInfo<>(orderService.selectAlldeliveryForMember(deliveryOrder));
        if (pageInfo.getPageNum()==1&pageNum>1){
            return null;
        }
        return pageInfo;
    };

    /**
     * 更新订单信息
     * @param shopOrder
     * @return
     */
    @RequestMapping("updateShopOrder")
    @ResponseBody
    public int updateShopOrder(ShopOrder shopOrder){
        return 1;
    }

    /**
     * 跑腿点击取货
     * @param ordersId
     * @return
     */
    @RequestMapping("getShopOrder")
    @ResponseBody
    public int getShopOrder(Long ordersId){
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setOrdersId(ordersId);
        shopOrder.setOrdersStatus("2");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        Date date=new Date();
        String creattime=sdf.format(date);
        shopOrder.setCreatTime(creattime);
        int msg=orderService.endOrders(shopOrder);
        return msg;
    }

    /**
     * 跑腿点击送达
     * @param ordersId
     * @return
     */
    @RequestMapping("endShopOrder")
    @ResponseBody
    public int endShopOrder(Long ordersId){
        return orderService.orderFulfillment(ordersId);
    }

    /**
     * 跑腿点击接单,向跑腿订单表插入订单数据,更新订单状态
     * @param ordersId
     * @param request
     * @return
     */
    @RequestMapping("EnableOrder")
    @ResponseBody
    public int EnableOrder(Long ordersId,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Delivery");
        ShopOrder shopOrder=orderService.selectOrder(ordersId);
        DeliveryOrder deliveryOrder=new DeliveryOrder();
        deliveryOrder.setOrdersId(ordersId);
        deliveryOrder.setDeliveryAddress(shopOrder.getOrdersAddress());
        deliveryOrder.setDeliveryUserId(member.getMemberId());
        deliveryOrder.setDeliveryTotal(shopOrder.getDeliveryTotal());
        deliveryOrder.setUserPhone(shopOrder.getUserPhone());
        deliveryOrder.setUserName(member.getMemberName());
        deliveryOrder.setDeliveryTotal(shopOrder.getDeliveryTotal());
        ShopOrder order=new ShopOrder();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        Date date=new Date();
        String creattime=sdf.format(date);
        order.setOrdersStatus("1");
        order.setCreatTime(creattime);
        order.setOrdersId(ordersId);
        orderService.endOrders(order);
        return deliveryOrderMapper.insert(deliveryOrder);
    }

    /**
     * 在订单结算后,增加物品,新建子订单
     * @param afterOrder
     * @param request
     * @return
     */
    @RequestMapping("AddAfterOrder")
    @ResponseBody
    public int AddAfterOrder(AfterOrder afterOrder,Long ordersId, @RequestParam(value = "productIds[]") String[] productIds, String people, HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String creatime=sdf.format(date);
        BigDecimal total=BigDecimal.ZERO;
        Product product=new Product();
        Map<Product,String> productMap=new HashMap<>();
        try{
            for (int i=0;i<productIds.length;i++) {
                String[] str2 = productIds[i].split("\\*");
                product = productService.selectProductNews(str2[0]);
                product.setOrderNum(str2[1]);
                OrderDetail orderDetail=new OrderDetail();
                orderDetail.setOrderId(ordersId);
                orderDetail.setOrderDetailNum(Integer.valueOf(str2[1]));
                orderDetail.setOrderDetailName(product.getProductName());
                orderDetail.setOrderDetailPng(product.getProductAvator());
                orderDetail.setOrderDetailTotal(product.getProductTotal());
                productService.orderdetailinsert(orderDetail);
            }
        afterOrder.setCreatTime(creatime);
        afterOrder.setAfterOrderId(NumberID.nextId(port));
        ShopOrder order=orderService.selectOrder(ordersId);
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setOrdersId(ordersId);
        shopOrder.setOrdersPeople(order.getOrdersPeople()+Integer.valueOf(people));
        orderService.endOrders(shopOrder);
        }catch (NullPointerException e){
            logger.info("空指针:"+e);
        }
        if (member==null){
            logger.info("用户未登录,阻止进行");
            return 2;
        }
        return orderService.AddAfterOrder(afterOrder);
    }

    /**
     * 查询子订单
     * @param orderId
     * @return
     */
    @RequestMapping("SelectAfterOrder")
    @ResponseBody
    public AfterOrder SelectAfterOrder(Long orderId){
        return orderService.SelectAfterOrder(orderId);
    }

    /**
     * 通过ordersId查询订单
     * @param orderIds
     * @param request
     * @return
     */
    @RequestMapping("selectOrder")
    @ResponseBody
    public ShopOrder selectOrder(Long orderIds,HttpServletRequest request){
        HttpSession session=request.getSession();
//        Member member=(Member)session.getAttribute("Student");
        return orderService.selectOrder(orderIds);
    }

    /**
     * 查询需要跑腿的订单
     * @param pageNum
     * @param request
     * @return
     */
    @RequestMapping("selectAlldelivery")
    @ResponseBody
    public PageInfo<DeliveryOrder> selectAlldelivery(int pageNum,HttpServletRequest request){
        HttpSession session=request.getSession();
        PageHelper.startPage(pageNum, 10);
        Member member=new Member();
        try{
            member=(Member)session.getAttribute("Delivery");
        }catch (NullPointerException e){
            logger.info("空指针异常"+e);
        }
        logger.info(member.toString());
        if (member==null||!member.getMemberType().equals("2")){
            logger.info("用户未登录,阻止进行");
            return null;
        }
        PageInfo<DeliveryOrder> pageInfo = new PageInfo<>(orderService.selectAlldelivery());
        return pageInfo;
    }

    /**
     * 得到当前商铺的排队人数
     * @param request
     * @param shopId
     * @return
     */
    @RequestMapping("getShopPeople")
    @ResponseBody
    public int getShopPeople(HttpServletRequest request,String shopId){
        return orderService.getShopPeople(shopId);
    }

    /**
     * 确认接单
     * @param orderId
     * @return
     */
    @RequestMapping("confirmOrder")
    @ResponseBody
    public int confirmOrder(Long orderId){
        return orderService.confirmOrder(orderId);
    }

    /**
     * 拒绝退款
     * @param orderId
     * @return
     */
    @RequestMapping("rejectRefund")
    @ResponseBody
    public int rejectRefund(Long orderId){
        return orderService.rejectRefund(orderId);
    }

    /**
     * 确认退款
     * @param orderId
     * @return
     */
    @RequestMapping("confirmRefund")
    @ResponseBody
    public int confirmRefund(Long orderId){
        return orderService.confirmRefund(orderId);
    }

    /**
     * 餐后结算
     * @param total
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping("afterOrderRefund")
    @ResponseBody
    public int afterOrderRefund(BigDecimal total,Long orderId,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Shop");
        return orderService.afterOrderRefund(orderId,total);
    }

    /**
     * 订单状态改为退款
     * @param ordersId
     * @param request
     * @return
     */
    @RequestMapping("goRefund")
    @ResponseBody
    public int goRefund(Long ordersId,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setOrdersStatus("5");
        shopOrder.setOrdersId(ordersId);
        return orderService.endOrders(shopOrder);
    }

    /**
     * 取货
     * @param ordersId
     * @param request
     * @return
     */
    @RequestMapping("getProduct")
    @ResponseBody
    public int getProduct(Long ordersId,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setOrdersStatus("6");
        shopOrder.setOrdersId(ordersId);
        return orderService.endOrders(shopOrder);
    }

    /**
     * 查询已结束订单
     * @param pageNum
     * @return
     */
    @RequestMapping("selectEndOrder")
    @ResponseBody
    public PageInfo<ShopOrder> selectEndOrder(int pageNum,HttpServletRequest request){
        HttpSession session=request.getSession();
        Member member=(Member)session.getAttribute("Student");
        Long stuId=member.getMemberId();
        return null;
    };

}
