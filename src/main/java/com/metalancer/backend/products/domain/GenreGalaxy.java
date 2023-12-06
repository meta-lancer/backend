package com.metalancer.backend.products.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GenreGalaxy extends Asset {

    @Builder
    public GenreGalaxy(Long productsId, String title, String thumbnail, Integer price) {
        super(productsId, title, thumbnail, price);
    }
}