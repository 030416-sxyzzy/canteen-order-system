package com.canteen.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单详情数据传输对象
 */
@Data
public class OrderDetailDTO {
    /**
     * 菜品ID
     */
    private Long dishId;

    /**
     * 套餐ID
     */
    private Long setmealId;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 价格
     */
    private BigDecimal price;
} 