package com.canteen.vo;

import lombok.Data;

/**
 * 套餐菜品视图对象
 */
@Data
public class SetmealDishVO {
    /**
     * 菜品ID
     */
    private Long dishId;

    /**
     * 菜品名称
     */
    private String dishName;

    /**
     * 菜品价格
     */
    private Double dishPrice;

    /**
     * 份数
     */
    private Integer copies;
} 