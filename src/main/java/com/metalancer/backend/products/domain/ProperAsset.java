package com.metalancer.backend.products.domain;

import lombok.Builder;

public class ProperAsset extends Asset {
    
    @Builder
    public ProperAsset(Long assetId, String title, String assetUrl, Integer price) {
        super(assetId, title, assetUrl, price);
    }
}