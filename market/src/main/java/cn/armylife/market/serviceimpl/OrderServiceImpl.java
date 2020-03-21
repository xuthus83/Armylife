package cn.armylife.market.serviceimpl;

import cn.armylife.common.domain.*;
import cn.armylife.market.util.MessageWechat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    MemberMapper memberMapper;

    @Autowired HairvipMapper hairvipMapper;

    @Autowired PaymentsMapper paymentsMapper;

    @Autowired
    RedisDao redisDao;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MessageWechat messageWechat;

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
        BigDecimal orderTotal=shopOrder.getOrderTotal();
        Member shop=new Member();
        if (orderExpress!=1){
        Long shopId= shopOrder.getShopId();
        shop=memberService.selectMemberforId(shopId);
        if ("HAIR0505".equals(shop.getAlid())&&productLists==null&&orderExpress==2){
            shopOrder.setOrdersStatus("3");
        }
        logger.info("orderExpress:"+orderExpress);
        }
        else  if (orderExpress==0){
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
            if ("REST0202".equals(shop.getAlid())){
                //获取商品数量餐盒费+1
                if (shopOrder.getOrdersType()==1||shopOrder.getOrdersType()==2){
                orderTotal=orderTotal.add(product.getProductBoxfee().multiply(new BigDecimal(num)));
//                    logger.info("餐盒金额:"+orderTotal);
                }
                //获取到店人数 餐盒费+1
                else if (shopOrder.getOrdersType()==3){
                    orderTotal=orderTotal.add(new BigDecimal(shopOrder.getOrdersPeople()+1));
//                    logger.info("到店人数金额:"+orderTotal);
                }}
        }}
        WXtemplate wXtemplate1=new WXtemplate();
        if (orderExpress==2){
            wXtemplate1.setTemplate("_9hSju78I4FRSgSShcMN08-e5zIUGdCp87YvXOBiMTo");
            wXtemplate1.setOpenid(shop.getMemberWechat());
            wXtemplate1.setFirst("您好,已有新订单!");
            wXtemplate1.setRemark1("点击可查看订单详情");
            Map<String,String> key1=new HashMap<>();
            key1.put("key1","查看详情");
            key1.put("key2",shopOrder.getCreatTime());
            key1.put("key3","理发店");
            key1.put("key4",shopOrder.getMemberName());
            key1.put("key5","已付款");
            wXtemplate1.setKey(key1);
            wXtemplate1.setKey(key1);
            wXtemplate1.setUrl("Business/OrderWechat.html?ordersId="+String.valueOf(shopOrder.getOrdersId()));
            messageWechat.newOrderService(wXtemplate1);
            Hairvip hairvip=hairvipMapper.selectHairvip(shopOrder.getStuId());
            if (hairvip!=null){
                int number=Integer.valueOf(hairvip.getHairvipNum());
                if (number==0){
                    return 0;
                }
                hairvip.setHairvipNum(String.valueOf(number-1));
                hairvipMapper.updateHair(hairvip);
            }
            shopOrder.setOrdersStatus("3");
        }
        shopOrder.setOrderTotal(orderTotal);
        shopOrder.setFirstProduct(firstproduct);
        shopOrder.setProductId(productIds);
        int msg=shopOrderMapper.insert(shopOrder);
        return msg;
    }



    public int insertMeiTuan(ShopOrder shopOrder){
        return shopOrderMapper.insert(shopOrder);
    };

    @Override
    public int VIPinsert(ShopOrder shopOrder){
        return shopOrderMapper.insert(shopOrder);
    }

    /**
     * 查询用户的订单(可查询用户,商铺)
     * @param shopOrder
     * @return
     */
    @Override
    public Page<ShopOrder> selectAllForMember(ShopOrder shopOrder){
        return shopOrderMapper.selectAllForMember(shopOrder);
    };

    /**
     * 可查询跑腿订单
     * @param deliveryOrder
     * @return
     */
    @Override
    public Page<DeliveryOrder> selectAlldeliveryForMember(DeliveryOrder deliveryOrder){
        return shopOrderMapper.selectAlldeliveryForMember(deliveryOrder);
    };

    /**
     * 更新订单状态
     * @param shopOrder
     * @return
     */
    @Override
    public int endOrders(ShopOrder shopOrder){
        return shopOrderMapper.updateShopOrder(shopOrder);
    };

    /**
     * 更新用户金额
     * @param ordersId
     * @return
     */
    @Override
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
        int msg1=shopOrderMapper.updateShopOrder(shopOrder);
        if ("HAIR0505".equals(order.getMember().getAlid())){
            List<ShopOrder> shopOrderList=shopOrderMapper.selectOrderForHair();
            for (int i=0;i<shopOrderList.size();i++){
                ShopOrder hairOrder=shopOrderList.get(i);
                logger.info("理发店开始消费后排队订单:"+hairOrder.toString());
                int num = shopOrderMapper.shopPeople(hairOrder.getOrdersId());
                logger.info("排队人数:"+num);
                if (num<3){
                    Member student=memberService.selectMemberforId(hairOrder.getStuId());
                    WXtemplate wXtemplate=new WXtemplate();
                    wXtemplate.setOpenid(student.getMemberWechat());
                    wXtemplate.setTemplate("4qL32-V24Fvljz3c1GynCqd2CzjKRiYHxsu9ke-08Ko");
                    wXtemplate.setUrl("ArmyStudents/OrderWechat.html?ordersId="+hairOrder.getOrdersId());
                    wXtemplate.setFirst("排队通知:你当前排第"+(num+1)+"位,为避免失效请及时前往理发");
                    wXtemplate.setRemark1("感谢您的使用!");
                    Map<String,String> key=new HashMap<>();
                    key.put("key1",shop.getShopName());
                    key.put("key2",String.valueOf(num+1));
                    key.put("key3",String.valueOf(num));
                    key.put("key4","排队中");
                    key.put("key5",sdf.format(new Date()));
                    wXtemplate.setKey(key);
                    messageWechat.newOrderService(wXtemplate);
                }
            }
        }
        return msg1;
    };

    /**
     * 外卖结束订单
     * @param orderId
     * @return
     */
    @Override
    public int orderFulfillment(Long orderId){
        ShopOrder shopOrder=shopOrderMapper.selectOrder(orderId);
        if (shopOrder.getIsexpress()==0) {
    //获取商家Id,更新商家金额
        Long shopId = shopOrder.getShopId();
        Member shop = memberService.selectMemberforId(shopId);
        BigDecimal oldAmount = shop.getMemberTotal();
        BigDecimal addAmount = shopOrder.getOrderTotal();
        BigDecimal newAmount = oldAmount.add(addAmount);
        shopOrderMapper.orderFulfillment(newAmount, shopId);
    }
        //获取跑腿Id,更新跑腿金额
        Long deliveryId=shopOrderMapper.selectdeliveryForOrder(orderId).getDeliveryUserId();
        DeliveryOrder deliveryOrder=deliveryOrderMapper.selectDeliveryFororderId(orderId);
        Member delivery=memberService.selectMemberforId(deliveryId);
        BigDecimal oldTotal=delivery.getMemberTotal();
        BigDecimal addTotal=deliveryOrder.getDeliveryTotal();
        BigDecimal num=new BigDecimal(0.98);
        BigDecimal newmoney=addTotal.multiply(num);
        logger.info("跑腿送达后的钱:"+newmoney+"跑腿费:"+addTotal+"本金:"+oldTotal);
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
    @Override
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
    @Override
    public AfterOrder SelectAfterOrder(Long ordersId){
        AfterOrder afterOrder=afterOrderMapper.SelectAfterOrder(ordersId);
        return afterOrder;
    };


    /**
     * 可查询跑腿订单
     * @return
     */
    @Override
    public List<DeliveryOrder> selectAlldelivery(){
        return shopOrderMapper.selectAlldelivery();
    };

    /**
     * 得到当前商铺排队人数(通过订单Id)
     * @return
     */
    @Override
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
    @Override
    public String rankMonthPayForShop(Long shopId){
        return shopOrderMapper.rankMonthPayForShop(shopId);
    };

    /**
     * 日销
     * @param shopId
     * @return
     */
    @Override
    public String rankDayPayForShop(Long shopId){
       return shopOrderMapper.rankDayPayForShop(shopId);
    };

    /**
     * 确认接单
     * @param orderId
     * @return
     */
    @Override
    public int confirmOrder(Long orderId){
        return shopOrderMapper.confirmOrder(orderId);
    };

    /**
     * 拒绝退款
     * @param orderId
     * @return
     */
    @Override
    public int rejectRefund(Long orderId){
        return shopOrderMapper.rejectRefund(orderId);
    };

    /**
     * 确认退款
     * @param orderId
     * @return
     */
    @Override
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
        String refundOrderId=String.valueOf(shopOrder.getOrdersId());
        BigDecimal orderTotal=totalfee;
        String order=String.valueOf(shopOrder.getOrdersId());
        Payments payments=payMentsService.selectPaments(orderId);
        int msg=1;
        switch (payments.getPayApp()){
            case "2":
                logger.info("支付宝退款");
                msg=payMentsService.Alipayrefund(refundOrderId,"退款申请",totalfee.toString(),"取消订单");
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
    @Override
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

    @Override
    public List<ShopOrder> selectAfterOrder(){
        return shopOrderMapper.selectAfterOrder();
    };

    @Override
    public int updateHairAmount(String total,Long memberId,Long payments){
        Payments payments1=new Payments();
        ShopOrder shopOrder=new ShopOrder();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if(paymentsMapper.selectPaymentsForId(payments)==null){
            return 0;
        }else{
            payments1=paymentsMapper.selectPaymentsForId(payments);
            shopOrder=shopOrderMapper.selectOrder(payments1.getOrderId());
        }
            if (hairvipMapper.selectHairvip(memberId)==null){
                Hairvip hairvip=new Hairvip();
                hairvip.setVipId(memberId);
                hairvip.setHairvipNum("11");
                hairvip.setCreatTime(sdf.format(new Date()));
                hairvip.setHaivipAddress(shopOrder.getOrdersAddress());
                hairvip.setIs(1);
                hairvip.setHairvipName(shopOrder.getMemberName());
                hairvip.setHairvipPhone(shopOrder.getUserPhone());
                logger.info("充值办理会员");
                hairvipMapper.insert(hairvip);
            }else {
                logger.info("增加次数");
               Hairvip hari=hairvipMapper.selectHairvip(memberId);
               int old=Integer.valueOf(hari.getHairvipNum());
               String newnum=String.valueOf(old+11);
            Hairvip hairvip=new Hairvip();
            hairvip.setHairvipNum(newnum);
            hairvip.setVipId(memberId);
            hairvipMapper.updateHair(hairvip);
        }
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

    /**
     * 更新到店用餐人数
     * @param shopOrder
     * @return
     */
    @Override
    public int updatePeopleForOrder(ShopOrder shopOrder){
        return shopOrderMapper.updatePeopleForOrder(shopOrder);
    };

    /**
     * 增加人数订单
     * @param shopOrder
     * @return
     */
    @Override
    public int creatPeople(ShopOrder shopOrder){
        return shopOrderMapper.insert(shopOrder);
    }

    /**
     * 查询所有已领取会员
     * @return
     */
    @Override
    public List<Hairvip> selectHairAll(){
        List<Hairvip> hairvips=hairvipMapper.searchAll();
        return hairvips;
    };

    /**
     * 通过会员名查询用户信息
     * @param vipName
     * @return
     */
    @Override
    public List<Hairvip> searchHairVip(String vipName){
        List<Hairvip> hairvips=hairvipMapper.searchHairVip(vipName);
        for (int i=0;i<hairvips.size();i++){
            ShopOrder shopOrder= shopOrderMapper.selectOrderForHairVip(hairvips.get(i).getVipId());
            hairvips.get(i).setShopOrder(shopOrder);
        }
        return hairvips;
    };

    /**
     * 商家控制用户的次数
     * @param hairvip
     * @return
     */
    @Override
    public int editHairNum(Hairvip hairvip,int code){
            Hairvip member=hairvipMapper.selectHairvip(hairvip.getVipId());
            int lastNum=Integer.valueOf(member.getHairvipNum());
            String num="";
            if(code==1){
                num=String.valueOf(lastNum+1);
            }else if(code==2){
                num=String.valueOf(lastNum-1);
            }
            Hairvip hairvip1=new Hairvip();
            hairvip1.setVipId(hairvip.getVipId());
            hairvip1.setHairvipNum(num);
            return hairvipMapper.updateHair(hairvip1);
    };

    /**
     * 查询所有跑腿
     * @return
     */
   public List<Member> allDelivery(){
       return memberMapper.allDelivery();
   };
}
