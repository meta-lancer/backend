package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductsCategory {

    private final Long categoryId;
    private final String categoryName;
    private final String categoryNameEn;

    @Builder
    public ProductsCategory(Long id, String categoryName, String categoryNameEn) {
        this.categoryId = id;
        this.categoryName = categoryName;
        this.categoryNameEn = categoryNameEn;
    }
}
