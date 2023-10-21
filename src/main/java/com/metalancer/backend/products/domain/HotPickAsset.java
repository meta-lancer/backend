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
    public HotPickAsset(Long productsId, String title, String thumbnail, Integer price) {
        super(productsId, title, thumbnail, price);
    }
}