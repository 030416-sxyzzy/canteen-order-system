package com.canteen.mapper;

import com.canteen.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 套餐Mapper接口
 */
@Mapper
public interface SetmealMapper {
    /**
     * 根据ID查询套餐
     * @param id 套餐ID
     * @return 套餐信息
     */
    Setmeal selectById(Long id);

    /**
     * 查询所有套餐
     * @return 套餐列表
     */
    List<Setmeal> selectAll();

    /**
     * 根据条件查询套餐
     * @param suitableCrowd 适合人群
     * @param vitamin 维生素类型
     * @return 套餐列表
     */
    List<Setmeal> selectByConditions(String suitableCrowd, String vitamin);
} 