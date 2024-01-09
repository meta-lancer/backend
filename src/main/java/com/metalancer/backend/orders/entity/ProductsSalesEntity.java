package com.metalancer.backend.orders.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.CurrencyType;
import com.metalancer.backend.common.constants.PaymentType;
import com.metalancer.backend.orders.domain.SettlementRequestList;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "product_sales")
@ToString
public class ProductsSalesEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 71621414241999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_sales_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creatorEntity;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrdersEntity ordersEntity;
    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity productsEntity;
    @Column(name = "orderer_Id", nullable = false)
    private Long ordererId;
    @Column(nullable = false)
    private String orderNo;
    private String orderProductNo;
    private BigDecimal price;
    private BigDecimal chargeRate;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;
    @Column(name = "is_settled", nullable = false, columnDefinition = "정산요청 가능여부를 갯수만으로 비교하지않기위해")
    private boolean settled = false;

    @Builder
    public ProductsSalesEntity(CreatorEntity creatorEntity, OrdersEntity ordersEntity,
        ProductsEntity productsEntity, Long ordererId, String orderNo, String orderProductNo,
        BigDecimal price, BigDecimal chargeRate, PaymentType paymentType, CurrencyType currency) {
        this.creatorEntity = creatorEntity;
        this.ordersEntity = ordersEntity;
        this.productsEntity = productsEntity;
        this.ordererId = ordererId;
        this.orderNo = orderNo;
        this.orderProductNo = orderProductNo;
        this.price = price;
        this.chargeRate = chargeRate;
        this.paymentType = paymentType;
        this.currency = currency;
    }

    public void setChargeRate(BigDecimal chargeRate) {
        this.chargeRate = chargeRate;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public void setSettled() {
        this.settled = true;
    }

    public SettlementRequestList toSettlementRequestList() {
        return SettlementRequestList.builder().assetsId(productsEntity.getId())
            .assetTitle(productsEntity.getTitle()).price(price).currencyType(currency)
            .chargeRate(chargeRate).paymentType(paymentType).build();
    }
}
