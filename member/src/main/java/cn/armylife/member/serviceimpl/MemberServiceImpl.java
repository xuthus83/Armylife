package cn.armylife.member.serviceimpl;

import cn.armylife.common.domain.Member;
import cn.armylife.common.domain.MemberDeliveryaddress;
import cn.armylife.common.domain.Product;
import cn.armylife.common.domain.Transactions;
import cn.armylife.member.mapper.*;
import cn.armylife.member.service.MemberService;
import cn.armylife.member.util.IsAlid;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MemberServiceImpl implements MemberService {
    Logger logger=Logger.getLogger("MemberServiceImpl.class");

    @Autowired private TransactionsMapper transactionsMapper;
    @Autowired private MemberMapper memberMapper;
    @Autowired private AlidMapper alidMapper;
    @Autowired private ProductMapper productMapper;
    @Autowired private IsAlid isa;
    @Autowired private MemberDeliveryaddressMapper memberDeliveryaddressMapper;

    @Override
    public Long insert(Member record){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        Date date=new Date();
        String registertime=sdf.format(date);
        record.setRegisterTime(registertime);
        record.setLastloginTime(registertime);
        int users=memberMapper.insert(record);
        Long id=record.getMemberId();
        return id;
    };

    /**
     * 用户登录
     * @param users
     * @return
     */
    @Override
    public Member loginUser(Member users){
        return memberMapper.loginUser(users);
    };

    /**
     * 登录商铺
     * @param member
     * @return
     */
    @Override
    public Member loginShop(Member member){
        return memberMapper.loginShop(member);
    };

    /**
     * 重载登录信息
     * @param memberId
     * @return
     */
    @Override
    public Member reloadSession(Long memberId){
        return memberMapper.reloadSession(memberId);
    };

    /**
     * 更新用户信息
     * @param member
     * @return
     */
    @Override
    public int updateMember(Member member){
        return memberMapper.updateMember(member);
    };

    /**
     * 检查用户openid是否注册
     * @param openid
     * @return
     */
    @Override
    public boolean inspectMemberForOpenId(String openid,String type){//验证通过,返回ture.失败返回false
        Member user=new Member();
        user.setMemberType(type);
        user.setMemberWechat(openid);
        Member member=memberMapper.inspectMemberForOpenId(user);
        if (member==null){
            return true;
        }
        return false;
    };

    /**
     * 检查用户phone是否注册
     * @param phone
     * @return
     */
    @Override
    public Boolean inspectMemberForPhone(String phone,String type){
        Member member=memberMapper.inspectMemberForPhone(phone,type);
        if (member==null){
            return true;
        }
        return false;
    };

    /**
     * 检验授权码
     * @param alid
     * @return
     */
    @Override
    public Boolean inspectShopForALID(String alid){
       if("FRUI0101".equals(alid)||"REST0202".equals(alid)||"BOOK0303".equals(alid)||"SHOT0404".equals(alid)||"HAIR0505".equals(alid)||"MTEA0606".equals(alid)){
           return true;
       }else {
           return false;
       }
    };

    /**
     * 更新跑腿手机号码
     * @param member
     * @return
     */
    @Override
    public int updateDeliveryPhone(Member member){
        return memberMapper.updateDeliveryPhone(member);
    };

    /**
     * 添加跑腿提现记录
     * @param record
     * @return
     */
    @Override
    public int deliveryinsert(Transactions record){
        return transactionsMapper.insert(record);
    };

    /**
     * 查询零钱明细
     * @param payUser
     * @return
     */
    @Override
    public Page<Transactions> selectForDelivery(Long payUser){
        return transactionsMapper.selectForDelivery(payUser);
    };

    /**
     * 通过memberId查询member
     * @param memberId
     * @return
     */
    @Override
    public Member selectMemberforId(Long memberId){
        return memberMapper.selectMemberforId(memberId);
    };

    /**
     * 通过appId查询商户信息
     * @param appId
     * @return
     */
    @Override
    public Member ShopNewForAppId(String appId){
        return memberMapper.ShopNewForAppId(appId);
    };

    /**
     * 通过tagId查找商品
     * @param shopId
     * @return
     */
    @Override
    public List<Product> productForTag(long shopId){
        return productMapper.productForTag(shopId);
    };

    /**
     * 设置商家的起送价格(total)
     * @param shopId
     * @param total
     * @return
     */
    @Override
    public int isdeliver(Long shopId,String total){
        return memberMapper.isdeliver(shopId,total);
    };

    @Override
    public Member loginDev(String code){
        Member member=new Member();
        member.setMemberWechat(code);
        member.setMemberType("1");
        return memberMapper.inspectMemberForOpenId(member);
    };

    /**
     * 添加用户收货地址
     * @param memberDeliveryaddress
     * @return
     */
    @Override
    public int addressInsert(MemberDeliveryaddress memberDeliveryaddress){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        memberDeliveryaddress.setCreatTime(new Date());
        return memberDeliveryaddressMapper.insert(memberDeliveryaddress);
    };

    /**
     * 删除收货地址
     * @param addressId
     * @return
     */
    @Override
    public int removeAddress(Long addressId){
        return memberDeliveryaddressMapper.removeAddress(addressId);
    };

    /**
     * 查询用户收货地址
     * @param memberId
     * @return
     */
    @Override
    public List<MemberDeliveryaddress> searchAddress(Long memberId){
        return memberDeliveryaddressMapper.searchAddress(memberId);
    };

    /**
     * 修改地址
     * @param memberDeliveryaddress
     * @return
     */
    @Override
    public int updateAddress(MemberDeliveryaddress memberDeliveryaddress){
        return memberDeliveryaddressMapper.updateAddress(memberDeliveryaddress);
    };
}
