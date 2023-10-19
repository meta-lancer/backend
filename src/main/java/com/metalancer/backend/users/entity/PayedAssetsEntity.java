package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.orders.entity.OrderPaymentEntity;
import com.metalancer.backend.orders.entity.OrderProductsEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.domain.PayedAssets;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity(name = "payed_assets")
@ToString
public class PayedAssetsEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309177241493115L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "payed_asset_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity products;

    @ManyToOne
    @JoinColumn(name = "order_products_id", nullable = false)
    private OrderProductsEntity orderProductsEntity;

    @ManyToOne
    @JoinColumn(name = "order_payment_id", nullable = false)
    private OrderPaymentEntity orderPaymentEntity;

    @Column(nullable = false)
    private Integer downloadedCnt = 0;

    @Column(nullable = false)
    private String downloadLink;

    @Builder
    public PayedAssetsEntity(User user, ProductsEntity products,
        OrderProductsEntity orderProductsEntity, OrderPaymentEntity orderPaymentEntity,
        String downloadLink) {
        this.user = user;
        this.products = products;
        this.orderProductsEntity = orderProductsEntity;
        this.orderPaymentEntity = orderPaymentEntity;
        this.downloadLink = downloadLink;
    }

    public PayedAssets toDomain() {
        User seller = orderProductsEntity.getProductsEntity().getCreatorEntity().getUser();
        return PayedAssets.builder()
            .payedAssetsId(id).orderNo(orderProductsEntity.getOrderNo())
            .orderProductNo(orderProductsEntity.getOrderProductNo()).productsId(products.getId())
            .title(products.getTitle()).purchasedAt(orderPaymentEntity.getPurchasedAt())
            .thumbnail(products.getThumbnail()).downloadedCnt(downloadedCnt)
            .downloadLink(downloadLink).sellerName(seller.getName())
            .sellerNickname(seller.getNickname()).sellerPhone(seller.getMobile())
            .price(orderPaymentEntity.getOrdersEntity().getTotalPrice())
            .build();
    }
}
