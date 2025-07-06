package com.canteen.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 套餐视图对象
 */
@Data
public class SetmealVO {
    /**
     * 套餐ID
     */
    private Long id;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐价格
     */
    private BigDecimal price;

    /**
     * 套餐描述
     */
    private String description;

    /**
     * 维生素类型，多个用逗号分隔
     */
    private String vitaminType;

    /**
     * 套餐图片
     */
    private String image;

    /**
     * 套餐状态 0-停售 1-起售
     */
    private Integer status;

    /**
     * 套餐状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 套餐包含的菜品
     */
    private List<SetmealDishVO> setmealDishes;
} 