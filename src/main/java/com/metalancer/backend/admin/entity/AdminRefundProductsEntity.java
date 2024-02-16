package com.metalancer.backend.admin.entity;


import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "admin_refund_products")
public class AdminRefundProductsEntity extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "admin_refund_products_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "admin_refund_id", nullable = false)
    private AdminRefundEntity adminRefundEntity;
    @ManyToOne
    @JoinColumn(name = "order_product_id", nullable = false)
    private OrderProductsEntity orderProductsEntity;
    @Column(nullable = false)
    private String productsTitle;
    @Column(nullable = false)
    private BigDecimal refundPrice;

    @Builder
    public AdminRefundProductsEntity(AdminRefundEntity adminRefundEntity,
        OrderProductsEntity orderProductsEntity, String productsTitle, BigDecimal refundPrice) {
        this.adminRefundEntity = adminRefundEntity;
        this.orderProductsEntity = orderProductsEntity;
        this.productsTitle = productsTitle;
        this.refundPrice = refundPrice;
    }
}
