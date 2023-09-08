package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersJpaRepository extends JpaRepository<OrdersEntity, Long> {

}
