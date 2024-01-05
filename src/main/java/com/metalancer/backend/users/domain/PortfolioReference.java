package com.metalancer.backend.users.domain;

import lombok.Builder;
import lombok.Getter;


@Getter
public class PortfolioReference {

    private String url;
    private int ord;

    @Builder
    public PortfolioReference(String url, int ord) {
        this.url = url;
        this.ord = ord;
    }
}
