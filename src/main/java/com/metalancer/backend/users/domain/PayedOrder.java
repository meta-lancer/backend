package com.metalancer.backend.users.domain;

import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.products.domain.RequestOption;
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
    private Double price;
    private String purchasedAt;
    private String receiptUrl;
    private RequestOption requestOption;

    @Builder
    public PayedOrder(Long orderId, String orderNo, String title, OrderStatus orderStatus,
        String payMethod, Double price, LocalDateTime purchasedAt,
        String receiptUrl) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.title = title;
        this.orderStatus = orderStatus;
        this.payMethod = payMethod;
        this.price = price;
        this.purchasedAt = Time.convertDateToStringWithDot(purchasedAt);
        this.receiptUrl = receiptUrl;
    }

    public void setRequestOption(RequestOption requestOption) {
        this.requestOption = requestOption;
    }
}
