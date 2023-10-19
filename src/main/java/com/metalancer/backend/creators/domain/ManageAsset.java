package com.metalancer.backend.creators.domain;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class ManageAsset {

    private Long productsId;
    private String thumbnail;
    private String title;
    private int wishCnt;
    private int viewCnt;
    private List<String> tagList;
    private int price;

    @Builder
    public ManageAsset(Long productsId, String thumbnail, String title, int viewCnt,
        int price) {
        this.productsId = productsId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.viewCnt = viewCnt;
        this.price = price;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public void setWishCnt(int wishCnt) {
        this.wishCnt = wishCnt;
    }
}
