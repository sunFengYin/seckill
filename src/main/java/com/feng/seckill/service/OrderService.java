package com.feng.seckill.service;

import com.feng.seckill.entity.Order;

public interface OrderService {

    Order createOrder(int userId, int itemId, int amount, Integer promotionId, String itemStockLogId);

    void createOrderAsync(int userId, int itemId, int amount, Integer promotionId);

}
