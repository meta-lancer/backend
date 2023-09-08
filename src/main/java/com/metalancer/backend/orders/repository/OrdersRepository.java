package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrdersEntity;

public interface OrdersRepository {

    void save(OrdersEntity ordersEntity);
}
