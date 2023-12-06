package com.metalancer.backend.orders.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.metalancer.backend.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    private int paymentPrice;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;
    @Column(nullable = false)
    private String receiptUrl;

    @Builder
    public OrderPaymentEntity(OrdersEntity ordersEntity, String impUid, String orderNo,
        String type, String title,
        String method, int paymentPrice, String currency, Date purchasedAt, String receiptUrl) {
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
    }
}
