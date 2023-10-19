package com.metalancer.backend.products.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.users.entity.User;
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
@Entity(name = "products_wish")
@ToString
public class ProductsWishEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309174141999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_wish_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity productsEntity;

    @Builder
    public ProductsWishEntity(User user, ProductsEntity productsEntity) {
        this.user = user;
        this.productsEntity = productsEntity;
    }

    public void restoreProductsWish() {
        restore();
    }

    public void deleteProductsWish() {
        delete();
    }
}
