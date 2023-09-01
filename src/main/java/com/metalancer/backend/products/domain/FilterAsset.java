package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FilterAsset extends Asset {

    @Builder
    public FilterAsset(Long assetId, String title, String assetUrl, Integer price) {
        super(assetId, title, assetUrl, price);
    }
}