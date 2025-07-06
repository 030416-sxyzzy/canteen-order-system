package com.canteen.service;

import com.canteen.dto.OrderDTO;
import com.canteen.vo.OrderVO;
import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {
    /**
     * 创建订单
     * @param orderDTO 订单信息
     * @return 订单ID
     */
    Long create(OrderDTO orderDTO);

    /**
     * 支付订单
     * @param id 订单ID
     */
    void pay(Long id);

    /**
     * 完成订单
     * @param id 订单ID
     */
    void complete(Long id);

    /**
     * 取消订单
     * @param id 订单ID
     */
    void cancel(Long id);

    /**
     * 根据ID查询订单
     * @param id 订单ID
     * @return 订单信息
     */
    OrderVO getById(Long id);

    /**
     * 根据用户ID查询订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<OrderVO> getByUserId(Long userId);

    /**
     * 查询所有订单
     * @return 订单列表
     */
    List<OrderVO> list();

    /**
     * 根据订单号查询订单列表
     * @param orderNo 订单号
     * @return 订单列表
     */
    List<OrderVO> list(String orderNo);
} 