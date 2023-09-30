package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TrendSpotlight extends Asset {

    @Builder
    public TrendSpotlight(Long assetId, String title, String assetUrl, Integer price) {
        super(assetId, title, assetUrl, price);
    }
}