package cn.armylife.admin.mapper;

import cn.armylife.admin.domain.AfterOrder;

import java.util.List;

public interface AfterOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table after_order
     *
     * @mbggenerated
     */
    int insert(AfterOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table after_order
     *
     * @mbggenerated
     */
    List<AfterOrder> selectAll();
}