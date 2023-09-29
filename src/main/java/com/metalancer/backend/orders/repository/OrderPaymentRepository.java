package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.OrderPaymentEntity;

public interface OrderPaymentRepository {

    void save(OrderPaymentEntity createdOrderPaymentEntity);

    OrderPaymentEntity findByOrderNo(String orderNo);
}
