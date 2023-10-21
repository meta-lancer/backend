package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FilterAsset extends Asset {

    @Builder
    public FilterAsset(Long productsId, String title, String thumbnail, Integer price) {
        super(productsId, title, thumbnail, price);
    }
}