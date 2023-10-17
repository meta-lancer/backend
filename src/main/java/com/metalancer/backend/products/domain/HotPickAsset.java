package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HotPickAsset extends Asset {

    private boolean hasWish;

    public void setWish(Boolean hasWish) {
        this.hasWish = hasWish;
    }

    @Builder
    public HotPickAsset(Long assetId, String title, String assetUrl, Integer price) {
        super(assetId, title, assetUrl, price);
    }
}