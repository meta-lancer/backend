package com.metalancer.backend.users.domain;

import com.metalancer.backend.common.constants.OrderStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;


@Getter
public class PayedOrder {

    private Long orderId;
    private String orderNo;
    private String title;
    private OrderStatus orderStatus;
    private String payMethod;
    private int price;
    private LocalDateTime purchasedAt;
    private String receiptUrl;

    @Builder
    public PayedOrder(Long orderId, String orderNo, String title, OrderStatus orderStatus,
        String payMethod, int price, LocalDateTime purchasedAt,
        String receiptUrl) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.title = title;
        this.orderStatus = orderStatus;
        this.payMethod = payMethod;
        this.price = price;
        this.purchasedAt = purchasedAt;
        this.receiptUrl = receiptUrl;
    }
}
