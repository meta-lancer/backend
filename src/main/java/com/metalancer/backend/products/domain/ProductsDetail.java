package com.metalancer.backend.products.domain;

import com.metalancer.backend.users.domain.Creator;
import java.util.List;
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
    private final Integer salePrice;
    private final double discount;
    private final double rate;
    private final int ratingCnt;
    private boolean hasWish;
    private boolean hasCart;
    private boolean hasOrder;
    private String assetDetail;
    private String assetNotice;
    private String assetCopyRight;
    private List<String> tagList;
    private AssetFile assetFile;

    @Builder
    public ProductsDetail(Long assetId, ProductsCategory category, Creator creator,
        String sharedLink,
        String title, int price, Integer salePrice, double discount, double rate, int ratingCnt) {
        this.assetId = assetId;
        this.category = category;
        this.creator = creator;
        this.sharedLink = sharedLink;
        this.title = title;
        this.price = price;
        this.salePrice = salePrice;
        this.discount = discount;
        this.rate = rate;
        this.ratingCnt = ratingCnt;
    }

    public void setHasWish(boolean hasWish) {
        this.hasWish = hasWish;
    }

    public void setHasCart(boolean hasCart) {
        this.hasCart = hasCart;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public void setAssetFile(AssetFile assetFile) {
        this.assetFile = assetFile;
    }
}
