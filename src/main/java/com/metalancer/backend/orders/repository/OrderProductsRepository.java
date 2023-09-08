package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import java.util.List;

public interface OrderProductsRepository {

    void save(OrderProductsEntity createdOrderProductsEntity);

    List<OrderProductsEntity> findAllByOrder(OrdersEntity createdOrdersEntity);
}
