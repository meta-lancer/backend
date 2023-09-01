package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.products.entity.Products;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "cart")
@ToString
public class CartEntity extends BaseTimeEntity implements Serializable {

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
    private Products products;

    public Cart toDomain() {
        return Cart.builder().cartId(id).title(products.getTitle())
            .price(products.getPrice()).build();
    }
}
