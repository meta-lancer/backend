package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.orders.domain.OrderProducts;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import java.util.List;
import java.util.stream.Collectors;
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

    @Override
    public List<OrderProducts> findAllProductsByOrder(OrdersEntity ordersEntity) {
        return findAllByOrder(ordersEntity).stream().map(OrderProductsEntity::toOrderProducts)
            .collect(
                Collectors.toList());
    }

    @Override
    public List<OrderProductsEntity> findAllByOrderProductStatusIsNotAndStatus(
        OrderStatus orderStatus, DataStatus dataStatus) {
        return orderProductsJpaRepository.findAllByOrderProductStatusIsNotAndStatus(orderStatus,
            dataStatus);
    }

    @Override
    public OrderProductsEntity findByOrderProductNo(String orderProductNo) {
        return orderProductsJpaRepository.findByOrderProductNo(orderProductNo);
    }

}
