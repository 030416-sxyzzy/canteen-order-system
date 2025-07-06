package com.canteen.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 报表统计Mapper接口
 */
@Mapper
public interface ReportMapper {
    /**
     * 获取交易总金额
     * @return 总金额
     */
    BigDecimal getTotalAmount();
    
    /**
     * 获取总交易次数
     * @return 总交易次数
     */
    Integer getTotalOrders();
    
    /**
     * 获取套餐销售统计
     * @return 套餐销售统计列表
     */
    List<Map<String, Object>> getSetmealSales();
    
    /**
     * 获取菜品销售统计
     * @return 菜品销售统计列表
     */
    List<Map<String, Object>> getDishSales();
    
    /**
     * 获取用户订单统计
     * @return 用户订单统计列表
     */
    List<Map<String, Object>> getUserOrders();
    
    /**
     * 获取用户总数
     * @return 用户总数
     */
    Integer getTotalUsers();
    
    /**
     * 获取订单统计数据
     * @return 订单统计数据列表
     */
    List<Map<String, Object>> getOrderStatistics();
} 