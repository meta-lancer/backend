package com.metalancer.backend.orders.repository;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import com.metalancer.backend.users.domain.PayedOrder;
import com.metalancer.backend.users.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderPaymentRepositoryImpl implements OrderPaymentRepository {

    private final OrderPaymentJpaRepository orderPaymentJpaRepository;

    @Override
    public void save(OrderPaymentEntity createdOrderPaymentEntity) {
        orderPaymentJpaRepository.save(createdOrderPaymentEntity);
    }

    @Override
    public OrderPaymentEntity findByOrderNo(String orderNo) {
        return orderPaymentJpaRepository.findByOrderNo(orderNo).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public Page<PayedOrder> findAllByUserWithDateOption(User foundUser, Pageable pageable,
        LocalDateTime beginAt, LocalDateTime endAt) {
        Page<OrderPaymentEntity> orderPaymentEntities = orderPaymentJpaRepository.findAllByPurchasedAtBetweenAndOrderer(
            beginAt, endAt, foundUser, pageable);
        List<PayedOrder> payedOrderList = new ArrayList<>();
        for (OrderPaymentEntity orderPaymentEntity : orderPaymentEntities) {
            LocalDateTime purchasedAt = orderPaymentEntity.getPurchasedAt();
            String payMethod = orderPaymentEntity.getMethod();
            String title = orderPaymentEntity.getTitle();
            String receiptUrl = orderPaymentEntity.getReceiptUrl();
            OrdersEntity ordersEntity = orderPaymentEntity.getOrdersEntity();
            PayedOrder payedOrder = ordersEntity.toPayedOrderDomain(payMethod, purchasedAt, title,
                receiptUrl);
            payedOrderList.add(payedOrder);
        }
        long count = orderPaymentJpaRepository.countAllByUser(foundUser);
        return new PageImpl<>(payedOrderList, pageable, count);
    }

    @Override
    public Page<PayedOrder> findAllByUserWithOrderStatusAndDateOption(User foundUser,
        Pageable pageable, LocalDateTime beginAt, LocalDateTime endAt, OrderStatus orderStatus) {
        Page<OrderPaymentEntity> orderPaymentEntities = orderPaymentJpaRepository.findAllByPurchasedAtBetweenAndOrdererAndOrderStatus(
            beginAt, endAt, foundUser, orderStatus, pageable);
        List<PayedOrder> payedOrderList = new ArrayList<>();
        for (OrderPaymentEntity orderPaymentEntity : orderPaymentEntities) {
            LocalDateTime purchasedAt = orderPaymentEntity.getPurchasedAt();
            String payMethod = orderPaymentEntity.getMethod();
            String title = orderPaymentEntity.getTitle();
            String receiptUrl = orderPaymentEntity.getReceiptUrl();
            OrdersEntity ordersEntity = orderPaymentEntity.getOrdersEntity();
            PayedOrder payedOrder = ordersEntity.toPayedOrderDomain(payMethod, purchasedAt, title,
                receiptUrl);
            payedOrderList.add(payedOrder);
        }
        long count = orderPaymentJpaRepository.countAllByUser(foundUser);
        return new PageImpl<>(payedOrderList, pageable, count);
    }
}
