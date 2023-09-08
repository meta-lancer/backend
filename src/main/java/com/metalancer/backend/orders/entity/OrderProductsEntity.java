package com.metalancer.backend.orders.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.ClaimType;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "order_product")
@ToString
public class OrderProductsEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 716215177241999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_product_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User orderer;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrdersEntity ordersEntity;
    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity productsEntity;
    @Column(nullable = false)
    private String orderNo;
    private String orderProductNo;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderProductStatus = OrderStatus.PAY_ING;
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;
    @Enumerated(EnumType.STRING)
    private String claimStatus;

    @Builder
    public OrderProductsEntity(User orderer, OrdersEntity ordersEntity,
        ProductsEntity productsEntity, String orderNo,
        String orderProductNo) {
        this.orderer = orderer;
        this.ordersEntity = ordersEntity;
        this.productsEntity = productsEntity;
        this.orderNo = orderNo;
        this.orderProductNo = orderProductNo;
    }
}
