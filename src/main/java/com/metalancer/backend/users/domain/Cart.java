package com.metalancer.backend.users.domain;

import com.metalancer.backend.products.domain.RequestOption;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Cart {

    private final Long cartId;
    private final Long assetId;
    private final String title;
    private final int price;
    private String thumbnail;
    private RequestOption requestOption;

    @Builder
    public Cart(Long cartId, Long assetId, String title, int price, String thumbnail,
        RequestOption requestOption) {
        this.cartId = cartId;
        this.assetId = assetId;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.requestOption = requestOption;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
