package com.othertwink.OnlineOrders.service;

import com.othertwink.OnlineOrders.model.Order;

public interface OrderService {
    Order createOrder(Order order);

    Order deleteOrder(Long id);

    Order findById(Long id);
}
