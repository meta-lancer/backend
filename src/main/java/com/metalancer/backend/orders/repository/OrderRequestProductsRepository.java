package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrderRequestProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRequestProductsRepository {

    void save(OrderRequestProductsEntity orderRequestProductsEntity);

    Page<OrderRequestProductsEntity> findAllOrderRequestProductsByCreator(
        CreatorEntity creatorEntity, Pageable pageable);
}
