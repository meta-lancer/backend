package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrderRequestProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRequestProductsJpaRepository extends
    JpaRepository<OrderRequestProductsEntity, Long> {

}
