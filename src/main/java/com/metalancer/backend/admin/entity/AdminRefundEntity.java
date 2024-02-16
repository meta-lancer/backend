package com.metalancer.backend.admin.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.orders.entity.OrdersEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "admin_refund")
public class AdminRefundEntity extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "admin_refund_id", nullable = false)
    private Long id;
    @Column(name = "admin_name", nullable = false)
    private String adminName;
    @JsonBackReference
    @ManyToOne
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
    private BigDecimal refundTotalPrice;
    @Column(nullable = false)
    private LocalDateTime refundedAt;
    private String reason;

    @Builder
    public AdminRefundEntity(String adminName, OrdersEntity ordersEntity, String impUid,
        String orderNo,
        String title, String type, String method, String currency, BigDecimal refundTotalPrice,
        LocalDateTime refundedAt, String reason) {
        this.adminName = adminName;
        this.ordersEntity = ordersEntity;
        this.impUid = impUid;
        this.orderNo = orderNo;
        this.title = title;
        this.type = type;
        this.method = method;
        this.currency = currency;
        this.refundTotalPrice = refundTotalPrice;
        this.refundedAt = refundedAt;
        this.reason = reason;
    }
}
