package com.hxm.eshop.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxm.common.utils.PageUtils;
import com.hxm.eshop.order.entity.OrderEntity;
import com.hxm.eshop.order.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author xiaoming
 * @email z596593851@gmail.com
 * @date 2021-04-27 16:26:19
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);

    void closeOrder(OrderEntity orderEntity);

    OrderEntity getOrderByOrderSn(String orderSn);

    PayVo getOrderPay(String orderSn);

    String handlePayResult(PayAsyncVo vo);

    PageUtils queryPageWithItem(Map<String, Object> params);
}

