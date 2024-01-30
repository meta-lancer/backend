package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RequestOption {

    private final Long requestOptionId;
    private final String content;
    private final int price;
    private final int ord;

    @Builder
    public RequestOption(Long requestOptionId, String content, int price, int ord) {
        this.requestOptionId = requestOptionId;
        this.content = content;
        this.price = price;
        this.ord = ord;
    }
}
