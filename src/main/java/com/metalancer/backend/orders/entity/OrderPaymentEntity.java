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
    private String orderNo;

    @Column(name = "RECEIPT_ID", nullable = false)
    private String receiptId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private int paymentPrice;

    @Column(nullable = false)
    private Date purchasedAt;

    @Builder
    public OrderPaymentEntity(OrdersEntity ordersEntity, String orderNo, String receiptId,
        String type,
        String method, int paymentPrice, Date purchasedAt) {
        this.ordersEntity = ordersEntity;
        this.orderNo = orderNo;
        this.receiptId = receiptId;
        this.type = type;
        this.method = method;
        this.paymentPrice = paymentPrice;
        this.purchasedAt = purchasedAt;
    }
}
