package cn.armylife.admin.mapper;

import cn.armylife.admin.domain.ShopOrder;

import java.util.List;

public interface ShopOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop_order
     *
     * @mbggenerated
     */
    int insert(ShopOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table shop_order
     *
     * @mbggenerated
     */
    List<ShopOrder> selectAll();
}