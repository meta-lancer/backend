package com.metalancer.backend.products.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.DataStatusException;
import com.metalancer.backend.creators.domain.ManageAsset;
import com.metalancer.backend.products.domain.GenreGalaxy;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.domain.TrendSpotlight;
import com.metalancer.backend.users.entity.CreatorEntity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products")
@ToString
public class ProductsEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309881533993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "products_category_id", nullable = false)
    private ProductsCategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creatorEntity;
    @Column(unique = true)
    private String sharedLink;
    private String title;
    private int price;
    private Integer salePrice = null;
    private double discount = 0.0;
    private double rate = 5;
    private int ratingCnt = 1;
    private int viewCnt = 0;

    // 이미지
    private String thumbnail;
    // 태그
    // 확장자
    // 제작 프로그램
    // 호환 프로그램
    // 상품 저작권 안내

    private String assetDetail;
    private String assetNotice;
    private String assetCopyRight;
    private String website;
    private String productionProgram;
    private String compatibleProgram;

    public void setActive() {
        active();
    }

    public void setDelete() {
        delete();
    }

    public void update() {
        // 제목
        // 가격
        // 상세정보
        // 이미지
    }

    public void setSharedLink() {
        String baseLink = "https://www.metaovis.com/details?share_id=";
        int length = 10;
        this.sharedLink = baseLink + RandomStringUtils.randomAlphanumeric(length);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void addViewCnt() {
        this.viewCnt++;
    }

    private int addRatingCnt() {
        return ratingCnt++;
    }

    private int deductRatingCnt() {
        return ratingCnt--;
    }

    public void addRate(int newRate) {
        int currentRatingCnt = this.ratingCnt;
        int newRatingCnt = addRatingCnt();
        this.rate = Math.round((rate * currentRatingCnt + newRate) / newRatingCnt * 10 / 10.0);
        if (checkRateRange()) {
            throw new BaseException(ErrorCode.INVALID_PARAMETER);
        }
    }

    public void cancelRate(int pastRate) {
        int currentRatingCnt = this.ratingCnt;
        int newRatingCnt = deductRatingCnt();
        this.rate = Math.round((rate * currentRatingCnt - pastRate) / newRatingCnt * 10 / 10.0);
        if (checkRateRange()) {
            throw new BaseException(ErrorCode.INVALID_PARAMETER);
        }
    }

    private boolean checkRateRange() {
        return rate > 5 || rate < 1;
    }

    public void changeRate(int pastRate, int newRate) {
        cancelRate(pastRate);
        addRate(newRate);
    }


    public void isProductsStatusEqualsActive() {
        DataStatus status = getStatus();
        String PRODUCTS_STATUS_ERROR = "products status error";
        switch (status) {
            case DELETED -> throw new DataStatusException(PRODUCTS_STATUS_ERROR, ErrorCode.STATUS_DELETED);
        }
    }

    @Builder
    public ProductsEntity(CreatorEntity creatorEntity,
                          ProductsCategoryEntity productsCategoryEntity,
                          String title,
                          int price, String thumbnail, String assetDetail, String assetNotice, String assetCopyRight,
                          String website,
                          List<String> productionProgram, String compatibleProgram) {
        this.creatorEntity = creatorEntity;
        this.category = productsCategoryEntity;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.assetDetail = assetDetail;
        this.assetNotice = assetNotice;
        this.assetCopyRight = assetCopyRight;
        this.website = website;
        this.productionProgram = productionProgram != null ? productionProgram.toString() : "";
        this.compatibleProgram = compatibleProgram;
        setSharedLink();
    }

    public ProductsDetail toProductsDetail() {
        return ProductsDetail.builder().productsId(id).category(category.toDomain()).creator(
                        creatorEntity.toDomain())
                .sharedLink(sharedLink).title(title).price(price).salePrice(salePrice)
                .discount(discount).rate(rate).ratingCnt(ratingCnt).build();
    }

    public HotPickAsset toHotPickAsset() {
        return HotPickAsset.builder().productsId(id).title(title).price(price).thumbnail("").build();
    }

    public ManageAsset toManageAsset() {
        return ManageAsset.builder().productsId(id).thumbnail(thumbnail).title(title).price(price)
                .viewCnt(viewCnt).build();
    }

    public void setSalePrice(int salePrice) {
        if (salePrice < price) {
            this.salePrice = salePrice;
        }
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void deleteProducts() {
        delete();
        ;
    }

    public GenreGalaxy toGenreGalaxy() {
        return GenreGalaxy.builder().productsId(id).title(title).thumbnail(thumbnail).price(price).build();
    }

    public TrendSpotlight toTrendSpotLight() {
        return TrendSpotlight.builder().productsId(id).title(title).thumbnail(thumbnail).price(price).build();
    }
}
