package com.metalancer.backend.creators.domain;

import com.metalancer.backend.products.domain.RequestOption;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class ManageCommission {

    private Long productsId;
    private String thumbnail;
    private String title;
    private int wishCnt;
    private int viewCnt;
    private List<String> tagList;
    private int price;
    private List<RequestOption> requestOptions;

    @Builder
    public ManageCommission(Long productsId, String thumbnail, String title, int viewCnt,
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

    public void setRequestOptions(List<RequestOption> requestOptions) {
        this.requestOptions = requestOptions;
    }
}
