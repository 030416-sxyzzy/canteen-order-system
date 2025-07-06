package com.canteen.service.impl;

import com.canteen.mapper.ReportMapper;
import com.canteen.service.ReportService;
import com.canteen.vo.ReportVO;
import com.canteen.vo.SetmealSalesVO;
import com.canteen.vo.DishSalesVO;
import com.canteen.vo.UserOrderVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报表统计服务实现类
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    public ReportVO getReport() {
        ReportVO reportVO = new ReportVO();
        reportVO.setTotalAmount(getTotalAmount());
        reportVO.setTotalOrders(getTotalOrders());
        reportVO.setSetmealSales(getSetmealSales());
        reportVO.setDishSales(getDishSales());
        reportVO.setUserOrders(getUserOrders());
        reportVO.setTimeRange("全部时间");
        return reportVO;
    }

    @Override
    public List<SetmealSalesVO> getSetmealSales() {
        List<Map<String, Object>> salesData = reportMapper.getSetmealSales();
        return salesData.stream().map(this::convertToSetmealSalesVO).collect(Collectors.toList());
    }

    @Override
    public List<DishSalesVO> getDishSales() {
        List<Map<String, Object>> salesData = reportMapper.getDishSales();
        return salesData.stream().map(this::convertToDishSalesVO).collect(Collectors.toList());
    }

    @Override
    public List<UserOrderVO> getUserOrders() {
        List<Map<String, Object>> userData = reportMapper.getUserOrders();
        return userData.stream().map(this::convertToUserOrderVO).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalAmount() {
        return reportMapper.getTotalAmount();
    }

    @Override
    public Integer getTotalOrders() {
        return reportMapper.getTotalOrders();
    }

    @Override
    public Integer getTotalUsers() {
        return reportMapper.getTotalUsers();
    }

    @Override
    public List<Map<String, Object>> getOrderStatistics() {
        return reportMapper.getOrderStatistics();
    }

    /**
     * 将Map转换为SetmealSalesVO
     */
    private SetmealSalesVO convertToSetmealSalesVO(Map<String, Object> data) {
        SetmealSalesVO vo = new SetmealSalesVO();
        vo.setSetmealId(((Number) data.get("setmealId")).longValue());
        vo.setSetmealName((String) data.get("setmealName"));
        vo.setSalesCount(((Number) data.get("salesCount")).intValue());
        vo.setSalesAmount((BigDecimal) data.get("salesAmount"));
        vo.setPrice((BigDecimal) data.get("price"));
        return vo;
    }

    /**
     * 将Map转换为DishSalesVO
     */
    private DishSalesVO convertToDishSalesVO(Map<String, Object> data) {
        DishSalesVO vo = new DishSalesVO();
        vo.setDishId(((Number) data.get("dishId")).longValue());
        vo.setDishName((String) data.get("dishName"));
        vo.setSalesCount(((Number) data.get("salesCount")).intValue());
        vo.setSalesAmount((BigDecimal) data.get("salesAmount"));
        vo.setPrice((BigDecimal) data.get("price"));
        return vo;
    }

    /**
     * 将Map转换为UserOrderVO
     */
    private UserOrderVO convertToUserOrderVO(Map<String, Object> data) {
        UserOrderVO vo = new UserOrderVO();
        vo.setUserId(((Number) data.get("userId")).longValue());
        vo.setUsername((String) data.get("username"));
        vo.setOrderCount(((Number) data.get("orderCount")).intValue());
        vo.setTotalAmount((BigDecimal) data.get("totalAmount"));
        vo.setUserType(((Number) data.get("userType")).intValue());
        vo.setUserTypeDesc(vo.getUserType() == 1 ? "学生" : "教师");
        return vo;
    }
} 