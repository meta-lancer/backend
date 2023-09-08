package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrdersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepository {

    private final OrdersJpaRepository ordersJpaRepository;

    @Override
    public void save(OrdersEntity ordersEntity) {
        ordersJpaRepository.save(ordersEntity);
    }
}
