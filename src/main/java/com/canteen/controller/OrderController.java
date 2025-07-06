package com.canteen.controller;

import com.canteen.common.Result;
import com.canteen.dto.OrderDTO;
import com.canteen.service.OrderService;
import com.canteen.vo.OrderVO;
import com.canteen.vo.OrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public Result<Long> create(@RequestBody OrderDTO orderDTO) {
        //orderDTO接收前端发送的json数据
        log.info("创建订单：{}", orderDTO);
        //调用orderService的create方法，创建订单
        Long orderId = orderService.create(orderDTO);
        //返回订单id
        return Result.success(orderId);
    }

    /**
     * 获取所有订单列表
     */
    @GetMapping
    public Result<List<OrderVO>> list(@RequestParam(required = false) String orderNo) {
        log.info("获取订单列表，订单号：{}", orderNo);
        List<OrderVO> orderVOList = orderService.list(orderNo);
        return Result.success(orderVOList);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<OrderVO> getById(@PathVariable Long id) {
        log.info("获取订单详情：{}", id);
        OrderVO orderVO = orderService.getById(id);
        return Result.success(orderVO);
    }

    /**
     * 获取订单详情列表
     */
    @GetMapping("/{id}/details")
    public Result<List<OrderDetailVO>> getOrderDetails(@PathVariable Long id) {
        log.info("获取订单详情列表：{}", id);
        OrderVO orderVO = orderService.getById(id);
        if (orderVO != null) {
            return Result.success(orderVO.getOrderDetails());
        }
        return Result.success(Collections.emptyList());
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequest request) {
        log.info("更新订单状态：{}，新状态：{}", id, request.getStatus());
        
        switch (request.getStatus()) {
            case 1:
                orderService.pay(id);
                break;
            case 2:
                orderService.complete(id);
                break;
            case 3:
                orderService.cancel(id);
                break;
            default:
                return Result.error("无效的订单状态");
        }
        
        return Result.success();
    }

    /**
     * 支付订单
     */
    @PostMapping("/{id}/pay")
    public Result<Void> pay(@PathVariable Long id) {
        log.info("支付订单：{}", id);
        orderService.pay(id);
        return Result.success();
    }

    /**
     * 完成订单
     */
    @PostMapping("/{id}/complete")
    public Result<Void> complete(@PathVariable Long id) {
        log.info("完成订单：{}", id);
        orderService.complete(id);
        return Result.success();
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        log.info("取消订单：{}", id);
        orderService.cancel(id);
        return Result.success();
    }

    /**
     * 获取用户订单历史
     */
    @GetMapping("/user/{userId}")
    public Result<List<OrderVO>> getByUserId(@PathVariable Long userId) {
        log.info("获取用户订单列表：{}", userId);
        List<OrderVO> orderVOList = orderService.getByUserId(userId);
        return Result.success(orderVOList);
    }

    /**
     * 订单状态更新请求
     */
    public static class OrderStatusUpdateRequest {
        private Integer status;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }
} 