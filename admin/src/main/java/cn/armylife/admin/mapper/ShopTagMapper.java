package cn.armylife.admin.mapper;

import cn.armylife.admin.domain.ShopTag;

import java.util.List;

public interface ShopTagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop_tag
     *
     * @mbggenerated
     */
    int insert(ShopTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop_tag
     *
     * @mbggenerated
     */
    List<ShopTag> selectAll();
}