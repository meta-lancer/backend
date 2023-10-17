package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
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

    @Override
    public OrdersEntity findEntityByOrderNo(String orderNo) {
        return ordersJpaRepository.findByOrderNo(orderNo).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }
}
