package com.metalancer.backend.products.entity;

import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.common.constants.USE_YN;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products_category")
@ToString
public class ProductsCategory extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309862541993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_category_id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String categoryName;
    @Column(nullable = false)
    private String categoryNameEn;
    @Enumerated(EnumType.STRING)
    private USE_YN useYn = USE_YN.YES;
    private int seq;

    public void update(String categoryName, String categoryNameEn, USE_YN useYn, int seq) {
        this.categoryName = categoryName;
        this.categoryNameEn = categoryNameEn;
        this.useYn = useYn;
        this.seq = seq;
    }

    @Builder
    public ProductsCategory(String categoryName, String categoryNameEn, int seq) {
        this.categoryName = categoryName;
        this.categoryNameEn = categoryNameEn;
        this.seq = seq;
    }
}
