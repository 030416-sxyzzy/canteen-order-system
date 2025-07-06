package com.canteen.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 用户订单统计视图对象
 */
@Data
public class UserOrderVO {
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 订单数量
     */
    private Integer orderCount;
    
    /**
     * 消费金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 用户类型
     */
    private Integer userType;
    
    /**
     * 用户类型描述
     */
    private String userTypeDesc;
} 