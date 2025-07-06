package com.canteen.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 报表统计视图对象
 */
@Data
public class ReportVO {
    /**
     * 交易总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 总交易次数
     */
    private Integer totalOrders;
    
    /**
     * 套餐购买统计
     */
    private List<SetmealSalesVO> setmealSales;
    
    /**
     * 菜品购买统计
     */
    private List<DishSalesVO> dishSales;
    
    /**
     * 用户购买统计
     */
    private List<UserOrderVO> userOrders;
    
    /**
     * 统计时间范围
     */
    private String timeRange;
} 