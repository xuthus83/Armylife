package cn.armylife.market.serviceimpl;

import cn.armylife.common.domain.*;
import cn.armylife.market.config.RedisDao;
import cn.armylife.market.feign.MemberService;
import cn.armylife.market.feign.PayMentsService;
import cn.armylife.market.mapper.*;
import cn.armylife.market.service.OrderService;
import com.github.pagehelper.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    ShopOrderMapper shopOrderMapper;

    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Autowired DeliveryOrderMapper deliveryOrderMapper;

    @Autowired
    AfterOrderMapper afterOrderMapper;

    @Autowired
    PayMentsService payMentsService;

    @Autowired
    MemberService memberService;

    @Autowired HairvipMapper hairvipMapper;

    @Autowired
    RedisDao redisDao;

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        return restTemplate.getForObject("http://SERVICE-HI/hi?name="+name,String.class);
    }

    @Override
    public int insert(ShopOrder shopOrder,List<Product> productLists,String orderExpress){
        OrderDetail orderDetail=new OrderDetail();
        Product product=new Product();
        String productIds="";
        String firstproduct="";
        logger.info("orderExpress:"+orderExpress);
        if (!orderExpress.equals("1")){
        for (int i=0;i<productLists.size();i++) {
            logger.info("执行获取商品");
            product=productLists.get(i);
            firstproduct=String.valueOf(productLists.get(0).getProductId());
            orderDetail.setOrderDetailName(product.getProductName());
            orderDetail.setOrderDetailPng(product.getProductAvator());
            orderDetail.setOrderDetailTotal(product.getProductTotal());
            orderDetail.setOrderId(shopOrder.getOrdersId());
            orderDetail.setOrderDetailNum(Integer.valueOf(product.getOrderNum()));
            productIds=productIds+","+product.getProductId();
            orderDetailMapper.insert(orderDetail);
        }}
        shopOrder.setFirstProduct(firstproduct);
        shopOrder.setProductId(productIds);
        return shopOrderMapper.insert(shopOrder);
    }

    @Override
    public int VIPinsert(ShopOrder shopOrder){
        return shopOrderMapper.insert(shopOrder);
    }

    /**
     * 查询用户的订单(可查询用户,商铺)
     * @param shopOrder
     * @return
     */
    public Page<ShopOrder> selectAllForMember(ShopOrder shopOrder){
        return shopOrderMapper.selectAllForMember(shopOrder);
    };

    /**
     * 可查询跑腿订单
     * @param deliveryOrder
     * @return
     */
    public Page<DeliveryOrder> selectAlldeliveryForMember(DeliveryOrder deliveryOrder){
        return shopOrderMapper.selectAlldeliveryForMember(deliveryOrder);
    };

    /**
     * 更新订单状态
     * @param shopOrder
     * @return
     */
    public int endOrders(ShopOrder shopOrder){
        return shopOrderMapper.updateShopOrder(shopOrder);
    };

    /**
     * 外卖结束订单
     * @param orderID
     * @return
     */
    public int orderFulfillment(Long orderID){
        ShopOrder shopOrder=shopOrderMapper.selectOrder(orderID);

        //获取商家Id,更新商家金额
        Long shopId=shopOrder.getShopId();
        Member shop=memberService.selectMemberforId(shopId);
        BigDecimal oldAmount=shop.getMemberTotal();
        BigDecimal addAmount=shopOrder.getOrderTotal();
        BigDecimal newAmount=oldAmount.add(addAmount);
        shopOrderMapper.orderFulfillment(newAmount,shopId);

        //获取跑腿Id,更新跑腿金额
        Long deliveryId=shopOrder.getDeliveryOrder().getDeliveryUserId();
        Member delivery=memberService.selectMemberforId(deliveryId);
        BigDecimal oldTotal=delivery.getMemberTotal();
        BigDecimal addTotal=shopOrder.getDeliveryTotal();
        BigDecimal newTotal=oldTotal.add(addTotal);
        shopOrderMapper.orderFulfillment(newTotal,deliveryId);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        ShopOrder order=new ShopOrder();
        order.setOrdersId(orderID);
        order.setCreatTime(sdf.format(new Date()));
        order.setOrdersStatus("3");
        return shopOrderMapper.updateShopOrder(order);
    };

    /**
     * 通过orderid查找订单
     * @param orderId
     * @return
     */
    @Override
    public ShopOrder selectOrder(Long orderId){
        ShopOrder shopOrder=shopOrderMapper.selectOrder(orderId);
        AfterOrder afterOrder=afterOrderMapper.SelectAfterOrder(orderId);
            if (afterOrder!=null) {
                shopOrder.setAfterOrder(afterOrder);
            }
        return shopOrder;
    };

    /**
     * 在订单结算后,增加物品,新建子订单
     * @param afterOrder
     * @return
     */
    public int AddAfterOrder(AfterOrder afterOrder){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        Date date=new Date();
        String creatTime=sdf.format(date);
        afterOrder.setCreatTime(creatTime);
        return afterOrderMapper.insert(afterOrder);
    };

    /**
     * 查询子订单
     * @param ordersId
     * @return
     */
    public AfterOrder SelectAfterOrder(Long ordersId){
        AfterOrder afterOrder=afterOrderMapper.SelectAfterOrder(ordersId);
        return afterOrder;
    };


    /**
     * 可查询跑腿订单
     * @return
     */
    public Page<DeliveryOrder> selectAlldelivery(){
        return shopOrderMapper.selectAlldelivery();
    };

    /**
     * 得到当前商铺排队人数
     * @return
     */
    public int getShopPeople(String shopId){
        String number=redisDao.getValue(shopId);
        return Integer.valueOf(number);
    };
    //增加店铺排队人数
    public void upShopPeople(String shopId){
        String number=redisDao.getValue(shopId);
        int num=Integer.valueOf(number)+1;
        redisDao.setKey(shopId,String.valueOf(num));
    };
    //减少店铺排队人数
    public void downShopPeople(String shopId){
        String number=redisDao.getValue(shopId);
        int num=Integer.valueOf(number)-1;
        redisDao.setKey(shopId,String.valueOf(num));
    };

    /**
     * 月销
     * @param shopId
     * @return
     */
    public String rankMonthPayForShop(Long shopId){
        return shopOrderMapper.rankMonthPayForShop(shopId);
    };

    /**
     * 日销
     * @param shopId
     * @return
     */
    public String rankDayPayForShop(Long shopId){
       return shopOrderMapper.rankDayPayForShop(shopId);
    };

    /**
     * 确认接单
     * @param orderId
     * @return
     */
    public int confirmOrder(Long orderId){
        return shopOrderMapper.confirmOrder(orderId);
    };

    /**
     * 拒绝退款
     * @param orderId
     * @return
     */
    public int rejectRefund(Long orderId){
        return shopOrderMapper.rejectRefund(orderId);
    };

    /**
     * 确认退款
     * @param orderId
     * @return
     */
    public int confirmRefund(Long orderId){
        ShopOrder shopOrder=shopOrderMapper.selectOrder(orderId);
        BigDecimal totalfee=shopOrder.getOrderTotal().add(shopOrder.getDeliveryTotal());
        String desc="订单退款";
        String refundOrderId=String.valueOf(shopOrder.getOrdersId());
        BigDecimal orderTotal=shopOrder.getOrderTotal().add(shopOrder.getDeliveryTotal());
        String order=String.valueOf(shopOrder.getOrdersId());
        String total=orderTotal.toString();
        String body="申请退款";
        Payments payments=payMentsService.selectPaments(orderId);
        int msg=1;
        switch (payments.getPayApp()){
            case "1":
                msg=payMentsService.Alipayrefund(order,desc,total,body);
                break;
            case "2":
                msg=payMentsService.WechatPayTransfer(Long.getLong(order),totalfee,orderTotal,"取消订单");
                break;
        }
        if (msg==0){
            return 0;
        }
        if (payMentsService.Alipayrefund(refundOrderId,desc,total,body)==0){
            return 0;
        };
        return shopOrderMapper.confirmOrder(orderId);
    };

    /**
     * 餐后结算
     * @param orderId
     * @return
     */
    public int afterOrderRefund(Long orderId, BigDecimal total){
        ShopOrder shopOrder=shopOrderMapper.selectOrder(orderId);
        Member member=shopOrder.getMember();
        BigDecimal totalfee=shopOrder.getOrderTotal().add(shopOrder.getDeliveryTotal());
        Payments payments=payMentsService.selectPaments(orderId);
        String order=String.valueOf(orderId);
        String desc="订单退款";
        String body="餐后结算";
        String amount=total.toString();
        int msg=1;
        switch (payments.getPayApp()){
            case "1":
                msg=payMentsService.Alipayrefund(order,desc,amount,body);
                break;
            case "2":
                msg=payMentsService.WechatPayTransfer(Long.getLong(order),totalfee,total,"餐后结算");
                break;
        }
        if (msg==0){
            return 0;
        }
        ShopOrder endOrder=new ShopOrder();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date=new Date();
        endOrder.setEndTime(date);
        endOrder.setOrdersId(orderId);
        DeliveryOrder deliveryOrder=deliveryOrderMapper.selectDeliveryFororderId(orderId);
        Member delivery=memberService.selectMemberforId(deliveryOrder.getDeliveryUserId());
        BigDecimal deliverytotal=delivery.getMemberTotal();
        BigDecimal newtotel=deliverytotal.add(total);
        Member newdelivery=new Member();
        newdelivery.setMemberId(delivery.getMemberId());
        newdelivery.setMemberTotal(newtotel);
        int message=memberService.updateMember(newdelivery);
        return shopOrderMapper.endOrder(shopOrder);

    };

    @Override
    public int orderdatailinsert(OrderDetail record){
        return orderDetailMapper.insert(record);
    };

    /**
     * 查询已结束订单
     * @param stuId
     * @return
     */
    @Override
    public Page<ShopOrder> selectEndOrder(Long stuId){
        return shopOrderMapper.selectEndOrder(stuId);
    };

}
