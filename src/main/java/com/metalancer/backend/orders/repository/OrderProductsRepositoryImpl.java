package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderProductsRepositoryImpl implements OrderProductsRepository {

    private final OrderProductsJpaRepository orderProductsJpaRepository;

    @Override
    public void save(OrderProductsEntity createdOrderProductsEntity) {
        orderProductsJpaRepository.save(createdOrderProductsEntity);
    }

    @Override
    public List<OrderProductsEntity> findAllByOrder(OrdersEntity createdOrdersEntity) {
        return orderProductsJpaRepository.findAllByOrdersEntity(createdOrdersEntity);
    }
}
