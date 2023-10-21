package com.metalancer.backend.products.domain;

import lombok.Getter;

import java.util.List;

@Getter // 부모 클래스의 getter가 없으면 못 받아온다.
public abstract class Asset {

    private Long productsId;
    private List<String> tagList;
    private String title;
    private String thumbnail;
    private Integer price;
    private Integer salePrice;

    public Asset(Long productsId, String title, String thumbnail, Integer price) {
        this.productsId = productsId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.price = price;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }
}
