package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductsJpaRepository extends JpaRepository<OrderProductsEntity, Long> {

    List<OrderProductsEntity> findAllByOrdersEntity(OrdersEntity ordersEntity);

    List<OrderProductsEntity> findAllByOrderProductStatusIsNotAndStatus(
        OrderStatus orderProductStatus,
        DataStatus status);

    OrderProductsEntity findByOrderProductNo(String orderProductNo);
}
