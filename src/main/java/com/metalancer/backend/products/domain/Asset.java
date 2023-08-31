package com.metalancer.backend.products.domain;

import java.util.List;

public abstract class Asset {

    private Long assetId;
    private List<String> tagList;
    private String title;
    private String assetUrl;
    private Integer price;
    private Integer salePrice;

    public Asset(Long assetId, String title, String assetUrl, Integer price) {
        this.assetId = assetId;
        this.title = title;
        this.assetUrl = assetUrl;
        this.price = price;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }
}
