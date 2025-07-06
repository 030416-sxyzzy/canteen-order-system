package com.canteen.mapper;

import com.canteen.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 套餐菜品关联Mapper接口
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据套餐ID查询套餐菜品关联
     * @param setmealId 套餐ID
     * @return 套餐菜品关联列表
     */
    List<SetmealDish> selectBySetmealId(Long setmealId);
} 