package com.canteen.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 套餐销售统计视图对象
 */
@Data
public class SetmealSalesVO {
    /**
     * 套餐ID
     */
    private Long setmealId;
    
    /**
     * 套餐名称
     */
    private String setmealName;
    
    /**
     * 购买数量
     */
    private Integer salesCount;
    
    /**
     * 销售金额
     */
    private BigDecimal salesAmount;
    
    /**
     * 套餐价格
     */
    private BigDecimal price;
} 