package com.metalancer.backend.orders.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.exception.DataStatusException;
import com.metalancer.backend.common.exception.InvalidParamException;
import com.metalancer.backend.orders.domain.CreatedOrder;
import com.metalancer.backend.users.domain.PayedOrder;
import com.metalancer.backend.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "orders")
@ToString
public class OrdersEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 716209177241999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User orderer;
    @Column(nullable = false)
    private String orderNo;
    @Column(nullable = false)
    private Integer totalPoint = 0;
    @Column(nullable = false)
    private Double totalPrice;
    @Column(nullable = false)
    private Double totalPaymentPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PAY_ING;

    @Builder
    public OrdersEntity(User orderer, String orderNo, Double totalPrice,
        Double totalPaymentPrice) {
        this.orderer = orderer;
        this.orderNo = orderNo;
        this.totalPoint = 0;
        this.totalPrice = totalPrice;
        if (totalPrice - this.totalPoint == totalPaymentPrice) {
            this.totalPaymentPrice = totalPaymentPrice;
        } else {
            throw new InvalidParamException("check totalPrice, totalPaymentPrice, totalPoint",
                ErrorCode.INVALID_PARAMETER);
        }
    }

    public CreatedOrder toCreatedOrderDomain() {
        return CreatedOrder.builder().ordererId(orderer.getId()).orderNo(orderNo)
            .totalPrice(totalPrice).orderStatus(orderStatus.getKorName())
            .buyerNm(orderer.getName()).buyerPhone(orderer.getMobile())
            .buyerEmail(orderer.getEmail())
            .build();
    }

    public PayedOrder toPayedOrderDomain(String payMethod, LocalDateTime purchasedAt, String title,
        String receiptUrl) {
        return PayedOrder.builder().orderId(id).orderNo(orderNo).orderStatus(orderStatus)
            .payMethod(payMethod).price(totalPrice).purchasedAt(purchasedAt)
            .title(title).receiptUrl(receiptUrl).build();
    }

    public void completeOrder() {
        // PAY_CONFIRM은 웹훅과 동시에 진행하기 때문에 허용
        if (this.orderStatus.equals(OrderStatus.PAY_ING) || this.orderStatus.equals(
            OrderStatus.PAY_CONFIRM)) {
            this.orderStatus = OrderStatus.PAY_CONFIRM;
        } else {
            throw new DataStatusException("올바르지않은 주문 상태 변경입니다.", ErrorCode.ILLEGAL_DATA_STATUS);
        }
    }

    public void completeOrderWithRequest() {
        // PAY_CONFIRM은 웹훅과 동시에 진행하기 때문에 허용
        if (this.orderStatus.equals(OrderStatus.PAY_ING) || this.orderStatus.equals(
            OrderStatus.PAY_DONE)) {
            this.orderStatus = OrderStatus.PAY_CONFIRM;
        } else {
            throw new DataStatusException("올바르지않은 주문 상태 변경입니다.", ErrorCode.ILLEGAL_DATA_STATUS);
        }
    }

    // 외주제작용
    public void confirmOrder() {
        if (this.orderStatus.equals(OrderStatus.PAY_DONE)) {
            this.orderStatus = OrderStatus.PAY_CONFIRM;
        } else {
            throw new DataStatusException("올바르지않은 주문 상태 변경입니다.", ErrorCode.ILLEGAL_DATA_STATUS);
        }
    }

    public void deleteOrder() {
        delete();
    }
}
