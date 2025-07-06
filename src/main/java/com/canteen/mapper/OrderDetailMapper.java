package com.canteen.mapper;

import com.canteen.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 订单详情Mapper接口
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量新增订单详情
     * @param orderDetails 订单详情列表
     */
    void insertBatch(List<OrderDetail> orderDetails);

    /**
     * 根据订单ID查询订单详情列表
     * @param orderId 订单ID
     * @return 订单详情列表
     */
    List<OrderDetail> selectByOrderId(Long orderId);

    /**
     * 根据订单ID删除订单详情
     * @param orderId 订单ID
     */
    void deleteByOrderId(Long orderId);
} 