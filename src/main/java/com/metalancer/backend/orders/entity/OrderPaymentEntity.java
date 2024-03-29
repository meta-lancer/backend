package com.metalancer.backend.orders.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.metalancer.backend.admin.domain.UserCompletedOrder;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.common.utils.Time;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "orders_payment")
public class OrderPaymentEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrdersEntity ordersEntity;
    @Column(nullable = false)
    private String impUid;
    @Column(nullable = false)
    private String orderNo;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal paymentPrice;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;
    //    @Column(nullable = false)
    private String receiptUrl;
    private String paidStatus;
    private String pgTid;

    @Builder
    public OrderPaymentEntity(OrdersEntity ordersEntity, String impUid, String orderNo,
        String type, String title,
        String method, BigDecimal paymentPrice, String currency, Date purchasedAt,
        String receiptUrl,
        String paidStatus, String pgTid) {
        this.ordersEntity = ordersEntity;
        this.impUid = impUid;
        this.orderNo = orderNo;
        this.type = type;
        this.method = method;
        this.title = title;
        this.paymentPrice = paymentPrice;
        this.currency = currency;
        this.receiptUrl = receiptUrl;
        this.purchasedAt = purchasedAt.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        ;
        this.paidStatus = paidStatus;
        this.pgTid = pgTid;
    }

    public UserCompletedOrder toUserCompletedOrder() {
        String paymentMethod = method;
        String paymentPgType = type;
        return UserCompletedOrder.builder()
            .user(ordersEntity.getOrderer().toUserDomain())
            .orderId(ordersEntity.getId())
            .orderNo(ordersEntity.getOrderNo())
            .orderStatus(ordersEntity.getOrderStatus())
            .price(paymentPrice)
            .currencyType("KRW".equals(currency) ? CurrencyType.KRW : CurrencyType.USD)
            .payMethod(PaymentType.getType(paymentMethod, paymentPgType))
            .purchasedAt(Time.convertDateToFullString(purchasedAt))
            .build();
    }
}
