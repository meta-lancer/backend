package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GenreGalaxy extends Asset {

    @Builder
    public GenreGalaxy(Long assetId, String title, String assetUrl, Integer price) {
        super(assetId, title, assetUrl, price);
    }
}