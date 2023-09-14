package com.metalancer.backend.products.entity;

import com.metalancer.backend.common.BaseTimeEntity;
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
@Entity(name = "products_views")
@ToString
public class ProductsViewsEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309177241912515L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_views_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity productsEntity;

    @Column(nullable = false)
    private String viewUrl;
    @Column(nullable = false)
    private int viewOrd;

    @Builder
    public ProductsViewsEntity(ProductsEntity productsEntity, String viewUrl, int viewOrd) {
        this.productsEntity = productsEntity;
        this.viewUrl = viewUrl;
        this.viewOrd = viewOrd;
    }
}
