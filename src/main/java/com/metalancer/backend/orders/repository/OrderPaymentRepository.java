package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import com.metalancer.backend.users.domain.PayedOrder;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderPaymentRepository {

    void save(OrderPaymentEntity createdOrderPaymentEntity);

    OrderPaymentEntity findByOrderNo(String orderNo);

    Page<PayedOrder> findAllByUserWithDateOption(User foundUser, Pageable pageable,
        LocalDateTime beginAt, LocalDateTime endAt);

    Page<PayedOrder> findAllByUserWithOrderStatusAndDateOption(User foundUser, Pageable pageable,
        LocalDateTime beginAt, LocalDateTime endAt, OrderStatus orderStatus);

    Page<OrderPaymentEntity> findAllByStatus(DataStatus dataStatus, Pageable pageable);
}
