package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import com.metalancer.backend.users.domain.Cart;
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
@Entity(name = "cart")
@ToString
public class CartEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309177241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "cart_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity products;

    @ManyToOne
    @JoinColumn(name = "products_request_option_id")
    private ProductsRequestOptionEntity productsRequestOptionEntity;

    @Builder
    public CartEntity(User user, ProductsEntity products,
        ProductsRequestOptionEntity productsRequestOptionEntity) {
        this.user = user;
        this.products = products;
        this.productsRequestOptionEntity = productsRequestOptionEntity;
    }

    public Cart toDomain() {
        return Cart.builder().cartId(id).assetId(products.getId()).title(products.getTitle())
            .price(products.getPrice()).requestOption(
                productsRequestOptionEntity != null ? productsRequestOptionEntity.toRequestOption()
                    : null)
            .thumbnail(products.getThumbnail())
            .build();
    }

    public void restoreCart() {
        restore();
    }

    public void deleteCart() {
        delete();
    }
}
