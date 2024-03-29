package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.orders.domain.OrderProducts;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import java.util.List;

public interface OrderProductsRepository {

    void save(OrderProductsEntity createdOrderProductsEntity);

    List<OrderProductsEntity> findAllByOrder(OrdersEntity createdOrdersEntity);

    List<OrderProducts> findAllProductsByOrder(OrdersEntity ordersEntity);

    List<OrderProductsEntity> findAllByOrderProductStatusIsNotAndStatus(OrderStatus orderStatus,
        DataStatus dataStatus);

}
