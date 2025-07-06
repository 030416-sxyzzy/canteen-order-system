package com.canteen.controller;

import com.canteen.common.Result;
import com.canteen.service.ReportService;
import com.canteen.vo.DishSalesVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 获取统计数据
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        log.info("获取统计数据");
        Map<String, Object> stats = new HashMap<>();
        BigDecimal totalRevenue = reportService.getTotalAmount();
        Integer totalOrders = reportService.getTotalOrders();
        Integer totalUsers = reportService.getTotalUsers();
        
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalOrders", totalOrders);
        stats.put("totalUsers", totalUsers);
        
        log.info("统计数据: 总收入={}, 总订单数={}, 用户总数={}", totalRevenue, totalOrders, totalUsers);
        return Result.success(stats);
    }

    /**
     * 获取订单数据
     */
    @GetMapping("/orders")
    public Result<List<Map<String, Object>>> getOrders() {
        log.info("获取订单数据");
        List<Map<String, Object>> orders = reportService.getOrderStatistics();
        log.info("订单数据: {}", orders);
        return Result.success(orders);
    }

    /**
     * 获取菜品数据
     */
    @GetMapping("/dishes")
    public Result<List<Map<String, Object>>> getDishes() {
        log.info("获取菜品数据");
        List<DishSalesVO> dishSales = reportService.getDishSales();
        List<Map<String, Object>> dishes = dishSales.stream()
                .map(dish -> {
                    Map<String, Object> dishMap = new HashMap<>();
                    dishMap.put("dishName", dish.getDishName());
                    dishMap.put("salesCount", dish.getSalesCount());
                    dishMap.put("revenue", dish.getSalesAmount());
                    return dishMap;
                })
                .collect(java.util.stream.Collectors.toList());
        log.info("菜品数据: {}", dishes);
        return Result.success(dishes);
    }
} 