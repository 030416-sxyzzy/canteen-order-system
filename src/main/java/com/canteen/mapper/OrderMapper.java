package com.canteen.mapper;

import com.canteen.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper {
    /**
     * 新增订单
     * @param order 订单信息
     */
    void insert(Order order);

    /**
     * 更新订单
     * @param order 订单信息
     */
    void update(Order order);

    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    Order selectById(Long id);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> selectByUserId(Long userId);

    /**
     * 查询所有订单
     * @return 订单列表
     */
    List<Order> selectAll();

    /**
     * 根据订单号查询订单列表
     * @param orderNo 订单号
     * @return 订单列表
     */
    List<Order> selectByOrderNo(String orderNo);

    /**
     * 获取当天订单数量
     * @return 当天订单数量
     */
    Integer getTodayOrderCount();
} 