package cn.armylife.admin.mapper;

import cn.armylife.admin.domain.MallArea;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MallAreaMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_area
     *
     * @mbggenerated
     */
    int insert(MallArea record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mall_area
     *
     * @mbggenerated
     */
    List<MallArea> selectAll();
}