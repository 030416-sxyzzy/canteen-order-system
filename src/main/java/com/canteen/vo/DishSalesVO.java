package com.canteen.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 菜品销售统计视图对象
 */
@Data
public class DishSalesVO {
    /**
     * 菜品ID
     */
    private Long dishId;
    
    /**
     * 菜品名称
     */
    private String dishName;
    
    /**
     * 购买数量
     */
    private Integer salesCount;
    
    /**
     * 销售金额
     */
    private BigDecimal salesAmount;
    
    /**
     * 菜品价格
     */
    private BigDecimal price;
} 