package com.canteen.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
public class OrderDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单详情列表
     */
    private List<OrderDetailDTO> orderDetails;
} 