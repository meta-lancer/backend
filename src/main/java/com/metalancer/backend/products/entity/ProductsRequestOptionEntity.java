package com.metalancer.backend.products.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.users.entity.CreatorEntity;
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
@Entity(name = "products_request_option")
@ToString
public class ProductsRequestOptionEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 74835274141999515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_request_option_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creatorEntity;

    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity productsEntity;

    private String title;
    private String content;
    private int price;
    private int ord;

    @Builder
    public ProductsRequestOptionEntity(CreatorEntity creatorEntity, ProductsEntity productsEntity,
        String title, String content, int price, int ord) {
        this.creatorEntity = creatorEntity;
        this.productsEntity = productsEntity;
        this.title = title;
        this.content = content;
        this.price = price;
        this.ord = ord;
    }

    public RequestOption toRequestOption() {
        return RequestOption.builder().requestOptionId(id).title(title).content(content)
            .price(price).ord(ord).build();
    }
}
