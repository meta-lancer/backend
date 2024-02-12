package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.orders.entity.OrderRequestProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRequestProductsJpaRepository extends
    JpaRepository<OrderRequestProductsEntity, Long> {

    Page<OrderRequestProductsEntity> findAllBySellerAndStatus(CreatorEntity seller,
        DataStatus status, Pageable pageable);
}
