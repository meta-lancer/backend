package com.metalancer.backend.users.domain;

import lombok.Builder;
import lombok.Getter;


@Getter
public class Cart {

    private final Long cartId;
    private final Long assetId;
    private final String title;
    private final int price;
    private String thumbnail;

    @Builder
    public Cart(Long cartId, Long assetId, String title, int price) {
        this.cartId = cartId;
        this.assetId = assetId;
        this.title = title;
        this.price = price;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
