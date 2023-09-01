package com.metalancer.backend.products.domain;

import com.metalancer.backend.products.entity.ProductsCategory;
import com.metalancer.backend.users.entity.Creator;
import lombok.Builder;
import lombok.Getter;


@Getter
public class ProductsDetail {

    private final Long assetId;
    private final ProductsCategory category;
    private final Creator creator;
    private final String sharedLink;
    private final String title;
    private final int price;
    private final double discount;
    private final double rate;
    private final int ratingCnt;
    private String thumbnail;

    @Builder
    public ProductsDetail(Long assetId, ProductsCategory category, Creator creator,
        String sharedLink,
        String title, int price, double discount, double rate, int ratingCnt) {
        this.assetId = assetId;
        this.category = category;
        this.creator = creator;
        this.sharedLink = sharedLink;
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.rate = rate;
        this.ratingCnt = ratingCnt;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
