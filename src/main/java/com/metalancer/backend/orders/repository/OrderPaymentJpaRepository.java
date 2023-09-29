package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPaymentJpaRepository extends JpaRepository<OrderPaymentEntity, Long> {

    Optional<OrderPaymentEntity> findByOrdersEntityAndStatus(OrdersEntity ordersEntity,
        DataStatus status);
}
