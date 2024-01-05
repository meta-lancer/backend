package com.metalancer.backend.products.domain;

import com.metalancer.backend.common.constants.ProductsType;
import com.metalancer.backend.users.domain.Creator;
import java.util.List;
import lombok.Builder;
import lombok.Getter;


@Getter
public class ProductsDetail {

    private final Long productsId;
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
    private String thumbnail;
    private List<String> thumbnailList;
    private List<RequestOption> requestOptionList;
    private ProductsType productsType;

    @Builder
    public ProductsDetail(Long productsId, Creator creator,
        String sharedLink,
        String title, int price, Integer salePrice, double discount, double rate, int ratingCnt,
        String assetDetail, String assetNotice, String assetCopyRight, ProductsType productsType) {
        this.productsId = productsId;
        this.creator = creator;
        this.sharedLink = sharedLink;
        this.title = title;
        this.price = price;
        this.salePrice = salePrice;
        this.discount = discount;
        this.rate = rate;
        this.ratingCnt = ratingCnt;
        this.assetDetail = assetDetail;
        this.assetNotice = assetNotice;
        this.assetCopyRight = assetCopyRight;
        this.productsType = productsType;
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

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setThumbnailList(List<String> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }

    public void setRequestOptionList(List<RequestOption> requestOptionList) {
        this.requestOptionList = requestOptionList;
    }
}
