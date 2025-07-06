package com.canteen.service.impl;

import com.canteen.dto.OrderDTO;
import com.canteen.dto.OrderDetailDTO;
import com.canteen.entity.Order;
import com.canteen.entity.OrderDetail;
import com.canteen.entity.Dish;
import com.canteen.entity.Setmeal;
import com.canteen.mapper.OrderMapper;
import com.canteen.mapper.OrderDetailMapper;
import com.canteen.mapper.DishMapper;
import com.canteen.mapper.SetmealMapper;
import com.canteen.mapper.UserMapper;
import com.canteen.service.OrderService;
import com.canteen.vo.OrderVO;
import com.canteen.vo.OrderDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;
    private final UserMapper userMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderDetailMapper orderDetailMapper,
                          DishMapper dishMapper, SetmealMapper setmealMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
        this.dishMapper = dishMapper;
        this.setmealMapper = setmealMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public Long create(OrderDTO orderDTO) {
        // 1. 创建订单
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setAmount(orderDTO.getAmount());
        order.setStatus(0); // 待支付
        order.setOrderNo(generateOrderNo());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        orderMapper.insert(order);
        
        // 2. 创建订单详情
        if (orderDTO.getOrderDetails() != null) {
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (OrderDetailDTO detailDTO : orderDTO.getOrderDetails()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(order.getId());
                detail.setDishId(detailDTO.getDishId());
                detail.setSetmealId(detailDTO.getSetmealId());
                detail.setQuantity(detailDTO.getQuantity());
                detail.setPrice(detailDTO.getPrice());
                detail.setCreateTime(LocalDateTime.now());
                detail.setUpdateTime(LocalDateTime.now());
                orderDetails.add(detail);
            }
            
            if (!orderDetails.isEmpty()) {
                orderDetailMapper.insertBatch(orderDetails);
            }
        }
        
        return order.getId();
    }

    @Override
    @Transactional
    public void pay(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确");
        }
        
        order.setStatus(1); // 已支付
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    @Transactional
    public void complete(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (order.getStatus() != 1) {
            throw new RuntimeException("订单状态不正确");
        }
        
        order.setStatus(2); // 已完成
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        
        if (order.getStatus() != 0) {
            throw new RuntimeException("订单状态不正确");
        }
        
        order.setStatus(3); // 已取消
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public OrderVO getById(Long id) {
        // 1. 查询订单基本信息
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return null;
        }
        
        // 2. 查询订单详情
        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(id);
        
        // 3. 转换为VO对象
        OrderVO orderVO = convertToVO(order);
        orderVO.setOrderDetails(convertToDetailVO(orderDetails));
        
        return orderVO;
    }

    @Override
    public List<OrderVO> getByUserId(Long userId) {
        // 1. 查询用户的所有订单
        List<Order> orders = orderMapper.selectByUserId(userId);
        
        // 2. 转换为VO对象
        return orders.stream().map(order -> {
            OrderVO orderVO = convertToVO(order);
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());
            orderVO.setOrderDetails(convertToDetailVO(orderDetails));
            return orderVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderVO> list() {
        // 1. 查询所有订单
        List<Order> orders = orderMapper.selectAll();
        
        // 2. 转换为VO对象
        return orders.stream().map(order -> {
            OrderVO orderVO = convertToVO(order);
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());
            orderVO.setOrderDetails(convertToDetailVO(orderDetails));
            return orderVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderVO> list(String orderNo) {
        // 1. 根据订单号查询订单
        List<Order> orders = orderMapper.selectByOrderNo(orderNo);
        
        // 2. 转换为VO对象
        return orders.stream().map(order -> {
            OrderVO orderVO = convertToVO(order);
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());
            orderVO.setOrderDetails(convertToDetailVO(orderDetails));
            return orderVO;
        }).collect(Collectors.toList());
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        // 获取当前日期，格式：yyyyMMdd
        String dateStr = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // 获取当天订单数量，用于生成序号
        int todayOrderCount = orderMapper.getTodayOrderCount();
        
        // 生成4位序号，不足补0
        String sequence = String.format("%04d", todayOrderCount + 1);
        
        return dateStr + sequence;
    }

    /**
     * 将Order对象转换为OrderVO对象
     */
    private OrderVO convertToVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setStatusDesc(getStatusDesc(order.getStatus()));
        orderVO.setUsername(userMapper.selectById(order.getUserId()).getUsername());
        return orderVO;
    }

    /**
     * 将OrderDetail对象列表转换为OrderDetailVO对象列表
     */
    private List<OrderDetailVO> convertToDetailVO(List<OrderDetail> orderDetails) {
        return orderDetails.stream().map(detail -> {
            OrderDetailVO detailVO = new OrderDetailVO();
            BeanUtils.copyProperties(detail, detailVO);
            
            // 设置菜品信息
            if (detail.getDishId() != null) {
                Dish dish = dishMapper.selectById(detail.getDishId());
                if (dish != null) {
                    detailVO.setDishName(dish.getName());
                }
            }
            
            // 设置套餐信息
            if (detail.getSetmealId() != null) {
                Setmeal setmeal = setmealMapper.selectById(detail.getSetmealId());
                if (setmeal != null) {
                    detailVO.setSetmealName(setmeal.getName());
                }
            }
            
            return detailVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取订单状态描述
     */
    private String getStatusDesc(Integer status) {
        switch (status) {
            case 0:
                return "待支付";
            case 1:
                return "已支付";
            case 2:
                return "已完成";
            case 3:
                return "已取消";
            default:
                return "未知状态";
        }
    }
} 