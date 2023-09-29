package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderPaymentRepositoryImpl implements OrderPaymentRepository {

    private final OrderPaymentJpaRepository orderPaymentJpaRepository;

    @Override
    public void save(OrderPaymentEntity createdOrderPaymentEntity) {
        orderPaymentJpaRepository.save(createdOrderPaymentEntity);
    }
}
