package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.orders.entity.OrderRequestProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRequestProductsRepositoryImpl implements OrderRequestProductsRepository {

    private final OrderRequestProductsJpaRepository orderRequestProductsJpaRepository;

    @Override
    public void save(OrderRequestProductsEntity orderRequestProductsEntity) {
        orderRequestProductsJpaRepository.save(orderRequestProductsEntity);
    }

    @Override
    public Page<OrderRequestProductsEntity> findAllOrderRequestProductsByCreator(
        CreatorEntity creatorEntity, Pageable pageable) {
        return orderRequestProductsJpaRepository.findAllBySellerAndStatus(
            creatorEntity, DataStatus.ACTIVE, pageable);
    }
}
