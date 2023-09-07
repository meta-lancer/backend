package com.metalancer.backend.products.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.StatusException;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.users.entity.CreatorEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;


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
    private String sharedLink;
    private String title;
    private int price;
    private Integer salePrice = null;
    private double discount = 0.0;
    private double rate = 5;
    private int ratingCnt = 1;
    private int viewCnt = 0;

    // 이미지
    // 태그
    // 확장자
    // 제작 프로그램
    // 호환 프로그램
    // 상품 저작권 안내

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
        int length = 10;
        this.sharedLink = RandomStringUtils.randomAlphanumeric(length);
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
            case DELETED ->
                throw new StatusException(PRODUCTS_STATUS_ERROR, ErrorCode.STATUS_DELETED);
        }
    }

    @Builder
    public ProductsEntity(CreatorEntity creatorEntity,
        ProductsCategoryEntity productsCategoryEntity,
        String title,
        int price) {
        this.creatorEntity = creatorEntity;
        this.category = productsCategoryEntity;
        this.title = title;
        this.price = price;
        setSharedLink();
    }

    public ProductsDetail toProductsDetail() {
        return ProductsDetail.builder().assetId(id).category(category.toDomain()).creator(
                creatorEntity.toDamain())
            .sharedLink(sharedLink).title(title).price(price).salePrice(salePrice)
            .discount(discount).rate(rate).ratingCnt(ratingCnt).build();
    }

    public HotPickAsset toHotPickAsset() {
        return HotPickAsset.builder().assetId(id).title(title).price(price).assetUrl("").build();
    }

    public void setSalePrice(int salePrice) {
        if (salePrice < price) {
            this.salePrice = salePrice;
        }
    }
}
