package com.canteen.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单详情视图对象
 */
@Data
public class OrderDetailVO {
    /**
     * 订单详情ID
     */
    private Long id;

    /**
     * 菜品ID
     */
    private Long dishId;

    /**
     * 菜品名称
     */
    private String dishName;

    /**
     * 套餐ID
     */
    private Long setmealId;

    /**
     * 套餐名称
     */
    private String setmealName;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 价格
     */
    private BigDecimal price;
} 