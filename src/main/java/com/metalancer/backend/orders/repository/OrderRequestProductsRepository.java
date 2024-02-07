package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrderRequestProductsEntity;

public interface OrderRequestProductsRepository {

    void save(OrderRequestProductsEntity orderRequestProductsEntity);
}
