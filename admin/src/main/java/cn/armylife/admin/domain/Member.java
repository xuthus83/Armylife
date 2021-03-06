package cn.armylife.admin.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Member implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_id
     *
     * @mbggenerated
     */
    private Long memberId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_type
     *
     * @mbggenerated
     */
    private String memberType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_idcard
     *
     * @mbggenerated
     */
    private String memberIdcard;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_wechat
     *
     * @mbggenerated
     */
    private String memberWechat;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_wechat_token
     *
     * @mbggenerated
     */
    private String memberWechatToken;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_alinumber
     *
     * @mbggenerated
     */
    private String memberAlinumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_ali_token
     *
     * @mbggenerated
     */
    private String memberAliToken;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_avatar
     *
     * @mbggenerated
     */
    private String memberAvatar;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_name
     *
     * @mbggenerated
     */
    private String memberName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_nickname
     *
     * @mbggenerated
     */
    private String memberNickname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.register_time
     *
     * @mbggenerated
     */
    private String registerTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.lastlogin_time
     *
     * @mbggenerated
     */
    private String lastloginTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_phone
     *
     * @mbggenerated
     */
    private String memberPhone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_total
     *
     * @mbggenerated
     */
    private BigDecimal memberTotal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_status
     *
     * @mbggenerated
     */
    private Integer memberStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.member_password
     *
     * @mbggenerated
     */
    private String memberPassword;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.shop_status
     *
     * @mbggenerated
     */
    private Integer shopStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.alid
     *
     * @mbggenerated
     */
    private String alid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column member.shop_name
     *
     * @mbggenerated
     */
    private String shopName;
    private String isdeliver;
    private String armylife;

    public String getArmylife() {
        return armylife;
    }

    public void setArmylife(String armylife) {
        this.armylife = armylife;
    }

    public String getIsdeliver() {
        return isdeliver;
    }

    public void setIsdeliver(String isdeliver) {
        this.isdeliver = isdeliver;
    }

    private int isHot;
    private List<ShopTag> shopTag;

    public List<ShopTag> getShopTag() {
        return shopTag;
    }

    public void setShopTag(List<ShopTag> shopTag) {
        this.shopTag = shopTag;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table member
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_id
     *
     * @return the value of member.member_id
     *
     * @mbggenerated
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_id
     *
     * @param memberId the value for member.member_id
     *
     * @mbggenerated
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_type
     *
     * @return the value of member.member_type
     *
     * @mbggenerated
     */
    public String getMemberType() {
        return memberType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_type
     *
     * @param memberType the value for member.member_type
     *
     * @mbggenerated
     */
    public void setMemberType(String memberType) {
        this.memberType = memberType == null ? null : memberType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_idcard
     *
     * @return the value of member.member_idcard
     *
     * @mbggenerated
     */
    public String getMemberIdcard() {
        return memberIdcard;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_idcard
     *
     * @param memberIdcard the value for member.member_idcard
     *
     * @mbggenerated
     */
    public void setMemberIdcard(String memberIdcard) {
        this.memberIdcard = memberIdcard == null ? null : memberIdcard.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_wechat
     *
     * @return the value of member.member_wechat
     *
     * @mbggenerated
     */
    public String getMemberWechat() {
        return memberWechat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_wechat
     *
     * @param memberWechat the value for member.member_wechat
     *
     * @mbggenerated
     */
    public void setMemberWechat(String memberWechat) {
        this.memberWechat = memberWechat == null ? null : memberWechat.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_wechat_token
     *
     * @return the value of member.member_wechat_token
     *
     * @mbggenerated
     */
    public String getMemberWechatToken() {
        return memberWechatToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_wechat_token
     *
     * @param memberWechatToken the value for member.member_wechat_token
     *
     * @mbggenerated
     */
    public void setMemberWechatToken(String memberWechatToken) {
        this.memberWechatToken = memberWechatToken == null ? null : memberWechatToken.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_alinumber
     *
     * @return the value of member.member_alinumber
     *
     * @mbggenerated
     */
    public String getMemberAlinumber() {
        return memberAlinumber;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_alinumber
     *
     * @param memberAlinumber the value for member.member_alinumber
     *
     * @mbggenerated
     */
    public void setMemberAlinumber(String memberAlinumber) {
        this.memberAlinumber = memberAlinumber == null ? null : memberAlinumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_ali_token
     *
     * @return the value of member.member_ali_token
     *
     * @mbggenerated
     */
    public String getMemberAliToken() {
        return memberAliToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_ali_token
     *
     * @param memberAliToken the value for member.member_ali_token
     *
     * @mbggenerated
     */
    public void setMemberAliToken(String memberAliToken) {
        this.memberAliToken = memberAliToken == null ? null : memberAliToken.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_avatar
     *
     * @return the value of member.member_avatar
     *
     * @mbggenerated
     */
    public String getMemberAvatar() {
        return memberAvatar;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_avatar
     *
     * @param memberAvatar the value for member.member_avatar
     *
     * @mbggenerated
     */
    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar == null ? null : memberAvatar.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_name
     *
     * @return the value of member.member_name
     *
     * @mbggenerated
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_name
     *
     * @param memberName the value for member.member_name
     *
     * @mbggenerated
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName == null ? null : memberName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_nickname
     *
     * @return the value of member.member_nickname
     *
     * @mbggenerated
     */
    public String getMemberNickname() {
        return memberNickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_nickname
     *
     * @param memberNickname the value for member.member_nickname
     *
     * @mbggenerated
     */
    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname == null ? null : memberNickname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.register_time
     *
     * @return the value of member.register_time
     *
     * @mbggenerated
     */
    public String getRegisterTime() {
        return registerTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.register_time
     *
     * @param registerTime the value for member.register_time
     *
     * @mbggenerated
     */
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime == null ? null : registerTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.lastlogin_time
     *
     * @return the value of member.lastlogin_time
     *
     * @mbggenerated
     */
    public String getLastloginTime() {
        return lastloginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.lastlogin_time
     *
     * @param lastloginTime the value for member.lastlogin_time
     *
     * @mbggenerated
     */
    public void setLastloginTime(String lastloginTime) {
        this.lastloginTime = lastloginTime == null ? null : lastloginTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_phone
     *
     * @return the value of member.member_phone
     *
     * @mbggenerated
     */
    public String getMemberPhone() {
        return memberPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_phone
     *
     * @param memberPhone the value for member.member_phone
     *
     * @mbggenerated
     */
    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone == null ? null : memberPhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_total
     *
     * @return the value of member.member_total
     *
     * @mbggenerated
     */
    public BigDecimal getMemberTotal() {
        return memberTotal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_total
     *
     * @param memberTotal the value for member.member_total
     *
     * @mbggenerated
     */
    public void setMemberTotal(BigDecimal memberTotal) {
        this.memberTotal = memberTotal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_status
     *
     * @return the value of member.member_status
     *
     * @mbggenerated
     */
    public Integer getMemberStatus() {
        return memberStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_status
     *
     * @param memberStatus the value for member.member_status
     *
     * @mbggenerated
     */
    public void setMemberStatus(Integer memberStatus) {
        this.memberStatus = memberStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.member_password
     *
     * @return the value of member.member_password
     *
     * @mbggenerated
     */
    public String getMemberPassword() {
        return memberPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.member_password
     *
     * @param memberPassword the value for member.member_password
     *
     * @mbggenerated
     */
    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword == null ? null : memberPassword.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.shop_status
     *
     * @return the value of member.shop_status
     *
     * @mbggenerated
     */
    public Integer getShopStatus() {
        return shopStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.shop_status
     *
     * @param shopStatus the value for member.shop_status
     *
     * @mbggenerated
     */
    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.alid
     *
     * @return the value of member.alid
     *
     * @mbggenerated
     */
    public String getAlid() {
        return alid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.alid
     *
     * @param alid the value for member.alid
     *
     * @mbggenerated
     */
    public void setAlid(String alid) {
        this.alid = alid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column member.shop_name
     *
     * @return the value of member.shop_name
     *
     * @mbggenerated
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column member.shop_name
     *
     * @param shopName the value for member.shop_name
     *
     * @mbggenerated
     */
    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table member
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", memberId=").append(memberId);
        sb.append(", memberType=").append(memberType);
        sb.append(", memberIdcard=").append(memberIdcard);
        sb.append(", memberWechat=").append(memberWechat);
        sb.append(", memberWechatToken=").append(memberWechatToken);
        sb.append(", memberAlinumber=").append(memberAlinumber);
        sb.append(", memberAliToken=").append(memberAliToken);
        sb.append(", memberAvatar=").append(memberAvatar);
        sb.append(", memberName=").append(memberName);
        sb.append(", memberNickname=").append(memberNickname);
        sb.append(", registerTime=").append(registerTime);
        sb.append(", lastloginTime=").append(lastloginTime);
        sb.append(", memberPhone=").append(memberPhone);
        sb.append(", memberTotal=").append(memberTotal);
        sb.append(", memberStatus=").append(memberStatus);
        sb.append(", memberPassword=").append(memberPassword);
        sb.append(", shopStatus=").append(shopStatus);
        sb.append(", alid=").append(alid);
        sb.append(", shopName=").append(shopName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}