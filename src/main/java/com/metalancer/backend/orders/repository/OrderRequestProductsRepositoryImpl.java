package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrderRequestProductsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRequestProductsRepositoryImpl implements OrderRequestProductsRepository {

    private final OrderRequestProductsJpaRepository orderRequestProductsJpaRepository;

    @Override
    public void save(OrderRequestProductsEntity orderRequestProductsEntity) {
        orderRequestProductsJpaRepository.save(orderRequestProductsEntity);
    }
}
