package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RequestOption {

    private final Long requestOptionId;
    private final String title;
    private final String content;
    private final int price;
    private final int ord;

    @Builder
    public RequestOption(Long requestOptionId, String title, String content, int price, int ord) {
        this.requestOptionId = requestOptionId;
        this.title = title;
        this.content = content;
        this.price = price;
        this.ord = ord;
    }
}
