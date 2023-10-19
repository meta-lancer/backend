package com.metalancer.backend.users.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.users.entity.PayedAssetsEntity;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PayedAssetsJpaRepository extends JpaRepository<PayedAssetsEntity, Long> {

    Page<PayedAssetsEntity> findAllByUserAndStatusOrderByCreatedAtDesc(User user, DataStatus status,
        Pageable pageable);

    @Query(
        "select pa from payed_assets pa where pa.orderPaymentEntity.purchasedAt between :beginAt and :endAt "
            + "and pa.orderPaymentEntity.ordersEntity.orderer = :user and pa.orderProductsEntity.orderProductStatus =:orderStatus and pa.status = 'ACTIVE'")
    Page<PayedAssetsEntity> findAllByUser(@Param("beginAt") LocalDateTime beginAt,
        @Param("endAt") LocalDateTime endAt,
        @Param("user") User user,
        @Param("orderStatus") OrderStatus orderStatus,
        Pageable pageable);

    @Query(
        "select pa from payed_assets pa where pa.orderPaymentEntity.purchasedAt between :beginAt and :endAt "
            + "and pa.orderPaymentEntity.ordersEntity.orderer = :user and pa.status = 'ACTIVE'")
    Page<PayedAssetsEntity> findAllByUser(@Param("beginAt") LocalDateTime beginAt,
        @Param("endAt") LocalDateTime endAt,
        @Param("user") User user,
        Pageable pageable);
}
