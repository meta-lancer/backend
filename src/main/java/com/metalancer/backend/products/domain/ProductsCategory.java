package com.metalancer.backend.products.domain;

import lombok.Builder;

public class ProductsCategory {

    private Long categoryId;
    private String categoryName;
    private String categoryNameEn;

    @Builder
    public ProductsCategory(Long id, String categoryName, String categoryNameEn) {
        this.categoryId = id;
        this.categoryName = categoryName;
        this.categoryNameEn = categoryNameEn;
    }
}
