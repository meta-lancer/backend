package com.metalancer.backend.orders.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.OrderStatus;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.creators.domain.CommissionSales;
import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "order_request_product")
@ToString
public class OrderRequestProductsEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 716215113241999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_product_request_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private CreatorEntity seller;
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrdersEntity ordersEntity;
    @ManyToOne
    @JoinColumn(name = "order_product_id", nullable = false)
    private OrderProductsEntity orderProductsEntity;
    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity productsEntity;
    @ManyToOne
    @JoinColumn(name = "products_request_option_id", nullable = false)
    private ProductsRequestOptionEntity productsRequestOptionEntity;
    private BigDecimal price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    @Column(nullable = false)
    private LocalDateTime payConfirmedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderProductStatus = OrderStatus.PAY_DONE;

    @Builder
    public OrderRequestProductsEntity(CreatorEntity seller, User buyer, OrdersEntity ordersEntity,
        OrderProductsEntity orderProductsEntity, ProductsEntity productsEntity,
        ProductsRequestOptionEntity productsRequestOptionEntity, BigDecimal price,
        CurrencyType currency, LocalDateTime purchasedAt) {
        this.seller = seller;
        this.buyer = buyer;
        this.ordersEntity = ordersEntity;
        this.orderProductsEntity = orderProductsEntity;
        this.productsEntity = productsEntity;
        this.productsRequestOptionEntity = productsRequestOptionEntity;
        this.price = price;
        this.currency = currency;
        this.purchasedAt = purchasedAt;
    }

    public CommissionSales toCommissionSales() {
        RequestOption requestOption = productsRequestOptionEntity.toRequestOption();
        return CommissionSales.builder().buyer(buyer).productsPrice(price)
            .requestOption(requestOption).orderStatus(orderProductStatus)
            .purchasedAt(Time.convertDateToFullString(purchasedAt))
            .payConfirmedAt(Time.convertDateToFullString(payConfirmedAt)).build();
    }

    public void payConfirm() {
        this.payConfirmedAt = LocalDateTime.now();
    }
}
