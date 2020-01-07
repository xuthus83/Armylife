package cn.armylife.admin.domain;

import java.io.Serializable;

public class ShopTag implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_tag.shop_tag_id
     *
     * @mbggenerated
     */
    private Long shopTagId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_tag.tag_id
     *
     * @mbggenerated
     */
    private Integer tagId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_tag.tag_name
     *
     * @mbggenerated
     */
    private String tagName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_tag.shop_id
     *
     * @mbggenerated
     */
    private Long shopId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_tag.creat_time
     *
     * @mbggenerated
     */
    private String creatTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table shop_tag
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_tag.shop_tag_id
     *
     * @return the value of shop_tag.shop_tag_id
     *
     * @mbggenerated
     */
    public Long getShopTagId() {
        return shopTagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_tag.shop_tag_id
     *
     * @param shopTagId the value for shop_tag.shop_tag_id
     *
     * @mbggenerated
     */
    public void setShopTagId(Long shopTagId) {
        this.shopTagId = shopTagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_tag.tag_id
     *
     * @return the value of shop_tag.tag_id
     *
     * @mbggenerated
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_tag.tag_id
     *
     * @param tagId the value for shop_tag.tag_id
     *
     * @mbggenerated
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_tag.tag_name
     *
     * @return the value of shop_tag.tag_name
     *
     * @mbggenerated
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_tag.tag_name
     *
     * @param tagName the value for shop_tag.tag_name
     *
     * @mbggenerated
     */
    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_tag.shop_id
     *
     * @return the value of shop_tag.shop_id
     *
     * @mbggenerated
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_tag.shop_id
     *
     * @param shopId the value for shop_tag.shop_id
     *
     * @mbggenerated
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_tag.creat_time
     *
     * @return the value of shop_tag.creat_time
     *
     * @mbggenerated
     */
    public String getCreatTime() {
        return creatTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_tag.creat_time
     *
     * @param creatTime the value for shop_tag.creat_time
     *
     * @mbggenerated
     */
    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime == null ? null : creatTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop_tag
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", shopTagId=").append(shopTagId);
        sb.append(", tagId=").append(tagId);
        sb.append(", tagName=").append(tagName);
        sb.append(", shopId=").append(shopId);
        sb.append(", creatTime=").append(creatTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}