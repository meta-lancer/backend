package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrdersEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersJpaRepository extends JpaRepository<OrdersEntity, Long> {

    Optional<OrdersEntity> findByOrderNo(String orderNo);
}
