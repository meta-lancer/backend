package com.metalancer.backend.creators.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class CreatorAssetList {

    private String thumbnail;
    private String title;
    private int price;
    private Long productsId;
    private boolean hasWish;

    @Builder
    public CreatorAssetList(String thumbnail, String title, int price, Long productsId) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.price = price;
        this.productsId = productsId;
    }
}
