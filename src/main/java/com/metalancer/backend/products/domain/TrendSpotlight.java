package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TrendSpotlight extends Asset {

    @Builder
    public TrendSpotlight(Long productsId, String title, String thumbnail, Integer price) {
        super(productsId, title, thumbnail, price);
    }
}