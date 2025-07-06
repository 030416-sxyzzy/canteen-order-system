package com.canteen.entity;

import lombok.Data;

/**
 * 套餐菜品关联实体类
 */
@Data
public class SetmealDish {
    /**
     * 主键
     */
    private Long id;

    /**
     * 套餐ID
     */
    private Long setmealId;

    /**
     * 菜品ID
     */
    private Long dishId;

    /**
     * 菜品名称（冗余字段）
     */
    private String dishName;

    /**
     * 菜品价格（冗余字段）
     */
    private Double dishPrice;

    /**
     * 份数
     */
    private Integer copies;
} 