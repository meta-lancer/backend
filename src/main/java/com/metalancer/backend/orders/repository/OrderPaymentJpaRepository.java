package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderPaymentJpaRepository extends JpaRepository<OrderPaymentEntity, Long> {

    Optional<OrderPaymentEntity> findByOrdersEntityAndStatus(OrdersEntity ordersEntity,
        DataStatus status);

    Optional<OrderPaymentEntity> findByOrderNo(String orderNo);

    @Query("select op from orders_payment op where op.purchasedAt between :beginAt and :endAt and op.ordersEntity.orderer = :user and op.status = 'ACTIVE'")
    Page<OrderPaymentEntity> findAllByPurchasedAtBetweenAndOrderer(
        @Param("beginAt") LocalDateTime beginAt,
        @Param("endAt") LocalDateTime endAt,
        @Param("user") User user,
        Pageable pageable);

    @Query("select count(op) from orders_payment op where op.ordersEntity.orderer = :user and op.status ='ACTIVE'")
    int countAllByUser(@Param("user") User user);
}
