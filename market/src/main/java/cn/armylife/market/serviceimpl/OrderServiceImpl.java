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

    @Autowired PaymentsMapper paymentsMapper;

    @Autowired
    RedisDao redisDao;

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hiError")
    public String hiService(String name) {
        return restTemplate.getForObject("http://SERVICE-HI/hi?name="+name,String.class);
    }

    @Override
    public int insert(ShopOrder shopOrder,List<Product> productLists,Integer orderExpress){
        OrderDetail orderDetail=new OrderDetail();
        Product product=new Product();
        String productIds="";
        String firstproduct="";
        Long shopId= shopOrder.getShopId();
        Member shop=memberService.selectMemberforId(shopId);
        logger.info("orderExpress:"+orderExpress);
        BigDecimal orderTotal=shopOrder.getOrderTotal();
        if (orderExpress==0){
            for (int i=0;i<productLists.size();i++) {
            logger.info("执行获取商品");
            product=productLists.get(i);
            firstproduct=String.valueOf(productLists.get(0).getProductId());
            orderDetail.setOrderDetailName(product.getProductName());
            orderDetail.setOrderDetailPng(product.getProductAvator());
            orderDetail.setOrderDetailTotal(product.getProductTotal());
            orderDetail.setOrderId(shopOrder.getOrdersId());
            int num=Integer.valueOf(product.getOrderNum());
            orderDetail.setOrderDetailNum(num);
            productIds=productIds+","+product.getProductId();
            orderDetailMapper.insert(orderDetail);
            if (shop.getAlid().equals("REST0202")){
                if (shopOrder.getOrdersType()==1||shopOrder.getOrdersType()==2){//获取商品数量餐盒费+1
                orderTotal=orderTotal.add(product.getProductBoxfee().multiply(new BigDecimal(num)));
                    logger.info("餐盒金额:"+orderTotal);
                }
                else if (shopOrder.getOrdersType()==3){//获取到店人数 餐盒费+1
                    orderTotal=orderTotal.add(new BigDecimal(shopOrder.getOrdersPeople()+1));
                    logger.info("到店人数金额:"+orderTotal);
                }}
        }}
        if (orderExpress==2){
            Hairvip hairvip=hairvipMapper.selectHairvip(shopOrder.getStuId());
            if (hairvip!=null){
                int number=Integer.valueOf(hairvip.getHairvipNum());
                hairvip.setHairvipNum(String.valueOf(number-1));
                hairvipMapper.updateHair(hairvip);
            }
            shopOrder.setOrdersStatus("3");
        }
        shopOrder.setOrderTotal(orderTotal);
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
     * 更新用户金额
     * @param ordersId
     * @return
     */
    public int updateMemberTotal(Long ordersId){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setOrdersStatus("6");
        shopOrder.setOrdersId(ordersId);
        shopOrder.setEndTime(new Date());
        ShopOrder order=shopOrderMapper.selectOrder(ordersId);
        Member shop=memberService.selectMemberforId(order.getShopId());
        BigDecimal oldtotal=shop.getMemberTotal();
        BigDecimal newtotal=oldtotal.add(order.getOrderTotal());
        int msg= shopOrderMapper.orderFulfillment(newtotal,shop.getMemberId());
        if (msg==0){
            return 0;
        }
        return shopOrderMapper.updateShopOrder(shopOrder);
    };
    /**
     * 外卖结束订单
     * @param orderId
     * @return
     */
    public int orderFulfillment(Long orderId){
        ShopOrder shopOrder=shopOrderMapper.selectOrder(orderId);

        //获取商家Id,更新商家金额
        Long shopId=shopOrder.getShopId();
        Member shop=memberService.selectMemberforId(shopId);
        BigDecimal oldAmount=shop.getMemberTotal();
        BigDecimal addAmount=shopOrder.getOrderTotal();
        BigDecimal newAmount=oldAmount.add(addAmount);
        shopOrderMapper.orderFulfillment(newAmount,shopId);

        //获取跑腿Id,更新跑腿金额
        Long deliveryId=shopOrderMapper.selectdeliveryForOrder(orderId).getDeliveryUserId();
        Member delivery=memberService.selectMemberforId(deliveryId);
        BigDecimal oldTotal=delivery.getMemberTotal();
        BigDecimal addTotal=shopOrder.getDeliveryTotal();
        BigDecimal num=new BigDecimal(0.9);
        BigDecimal newmoney=addTotal.multiply(num);
        logger.info("跑腿送达后的钱:"+newmoney+"跑腿费:"+addAmount+"本金:"+oldTotal);
        BigDecimal newTotal=oldTotal.add(newmoney);
        shopOrderMapper.orderFulfillment(newTotal,deliveryId);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        ShopOrder order=new ShopOrder();
        order.setOrdersId(orderId);
        order.setCreatTime(sdf.format(new Date()));
        order.setOrdersStatus("6");
        order.setEndTime(new Date());
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
    public List<DeliveryOrder> selectAlldelivery(){
        return shopOrderMapper.selectAlldelivery();
    };

    /**
     * 得到当前商铺排队人数(通过订单Id)
     * @return
     */
    public int getShopPeople(Long ordersId){
        return shopOrderMapper.shopPeople(ordersId);
    };
//    //增加店铺排队人数
//    public void upShopPeople(String shopId){
//        String number=redisDao.getValue(shopId);
//        int num=Integer.valueOf(number)+1;
//        redisDao.setKey(shopId,String.valueOf(num));
//    };
//    //减少店铺排队人数
//    public void downShopPeople(String shopId){
//        String number=redisDao.getValue(shopId);
//        int num=Integer.valueOf(number)-1;
//        redisDao.setKey(shopId,String.valueOf(num));
//    };

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
        BigDecimal totalfee=null;
        try {
            if (shopOrder.getDeliveryTotal() != null) {
                totalfee = shopOrder.getOrderTotal().add(shopOrder.getDeliveryTotal());
            }else {
            totalfee=shopOrder.getOrderTotal();}
        }catch (NullPointerException e){
            e.printStackTrace();
            totalfee=shopOrder.getOrderTotal();
        }
        logger.info("退款资金"+totalfee);
        String desc="订单退款";
        String refundOrderId=String.valueOf(shopOrder.getOrdersId());
        BigDecimal orderTotal=totalfee;
        String order=String.valueOf(shopOrder.getOrdersId());
        Payments payments=payMentsService.selectPaments(orderId);
        int msg=1;
        switch (payments.getPayApp()){
            case "2":
                logger.info("支付宝退款");
                msg=payMentsService.Alipayrefund(String.valueOf(orderId),"退款申请",totalfee.toString(),"取消订单");
                if (msg==0){
                    return 0;
                }
                break;
            case "1":
                logger.info("微信退款");
                msg=payMentsService.WechatPayTransfer(Long.valueOf(order),totalfee,orderTotal,"取消订单");
                if (msg==0){
                    return 0;
                }
                break;
        }
        return shopOrderMapper.confirmRefund(orderId);
    };

    /**
     * 餐后结算
     * @param orderId
     * @return
     */
    public int afterOrderRefund(Long orderId, BigDecimal total){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ShopOrder shopOrder=shopOrderMapper.selectOrder(orderId);
        Member member=shopOrder.getMember();
        BigDecimal totalfee=shopOrder.getOrderTotal();
        Payments payments=payMentsService.selectPaments(orderId);
        String order=String.valueOf(orderId);
        String desc="订单退款";
        String body="餐后结算";
        String amount=total.toString();
        BigDecimal oldTotal=member.getMemberTotal();
        BigDecimal newTotal=oldTotal.subtract(total);
        shopOrderMapper.orderFulfillment(newTotal,shopOrder.getShopId());
        Payments payments1=new Payments();
        payments1.setPayRefund(1);
        payments1.setRefundTotal(total);
        payments1.setRefundDesc("订单结算");
        payments1.setPaymentsId(payments.getPaymentsId());
        payments1.setRefunsTime(sdf.format(new Date()));
        shopOrderMapper.updatePayments(payments1);
        int msg=1;
        switch (payments.getPayApp()){
            case "2":
                msg=payMentsService.Alipayrefund(order,body,amount,desc);
                break;
            case "1":
                msg=payMentsService.WechatPayTransfer(orderId,total,totalfee,"餐后结算");
                break;
        }
        if (msg==0){
            return 0;
        }
        ShopOrder endOrder=new ShopOrder();
        Date date=new Date();
        endOrder.setEndTime(date);
        endOrder.setOrdersId(orderId);
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

    public List<ShopOrder> selectAfterOrder(){
        return shopOrderMapper.selectAfterOrder();
    };

    @Override
    public int updateHairAmount(String total,Long memberId){
        Member member=memberService.selectMemberforId(memberId);
        BigDecimal oldtotal=member.getMemberTotal();
        BigDecimal newtotal=oldtotal.add(new BigDecimal(total));
        return shopOrderMapper.orderFulfillment(newtotal,memberId);
    };

    /**
     * 查找支付订单
     * @param paymentsId
     * @return
     */
    @Override
    public Payments selectPaymentsForId(Long paymentsId){
        return paymentsMapper.selectPaymentsForId(paymentsId);
    };


}
