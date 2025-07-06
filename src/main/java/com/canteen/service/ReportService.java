package com.canteen.service;

import com.canteen.vo.ReportVO;
import com.canteen.vo.SetmealSalesVO;
import com.canteen.vo.DishSalesVO;
import com.canteen.vo.UserOrderVO;
import java.util.List;
import java.util.Map;

/**
 * 报表统计服务接口
 */
public interface ReportService {
    /**
     * 获取完整报表统计
     * @return 报表统计信息
     */
    ReportVO getReport();
    
    /**
     * 获取套餐销售统计
     * @return 套餐销售统计列表
     */
    List<SetmealSalesVO> getSetmealSales();
    
    /**
     * 获取菜品销售统计
     * @return 菜品销售统计列表
     */
    List<DishSalesVO> getDishSales();
    
    /**
     * 获取用户订单统计
     * @return 用户订单统计列表
     */
    List<UserOrderVO> getUserOrders();
    
    /**
     * 获取交易总金额
     * @return 总金额
     */
    java.math.BigDecimal getTotalAmount();
    
    /**
     * 获取总交易次数
     * @return 总交易次数
     */
    Integer getTotalOrders();
    
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