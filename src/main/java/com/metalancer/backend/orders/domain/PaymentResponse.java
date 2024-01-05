package com.metalancer.backend.orders.domain;

import com.metalancer.backend.common.constants.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentResponse {

    private final Long ordererId;
    private final String orderNo;
    private final BigDecimal totalPrice;
    private final BigDecimal totalPayment;
    private final BigDecimal totalPoint;
    private final OrderStatus orderStatus;
    private final String ordererNm;
    private final String ordererPhone;
    private final String ordererEmail;
    private final LocalDateTime purchasedAt;
    private List<OrderProducts> orderProductList;

    @Builder
    public PaymentResponse(Long ordererId, String orderNo, BigDecimal totalPrice,
        BigDecimal totalPayment, BigDecimal totalPoint, OrderStatus orderStatus, String ordererNm,
        String ordererPhone, String ordererEmail,
        Date purchasedAt) {
        this.ordererId = ordererId;
        this.orderNo = orderNo;
        this.totalPrice = totalPrice;
        this.totalPayment = totalPayment;
        this.totalPoint = totalPoint;
        this.orderStatus = orderStatus;
        this.ordererNm = ordererNm;
        this.ordererPhone = ordererPhone;
        this.ordererEmail = ordererEmail;
        this.purchasedAt = purchasedAt.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        ;
    }

    public void setOrderProductList(List<OrderProducts> orderProductList) {
        this.orderProductList = orderProductList;
    }
}