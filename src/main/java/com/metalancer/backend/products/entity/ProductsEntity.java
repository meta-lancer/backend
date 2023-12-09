package com.metalancer.backend.products.entity;

import com.metalancer.backend.admin.domain.ProductsList;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.DataStatusException;
import com.metalancer.backend.creators.domain.ManageAsset;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetUpdate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetUpdateWithOutThumbnail;
import com.metalancer.backend.products.domain.FilterAsset;
import com.metalancer.backend.products.domain.GenreGalaxy;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.domain.TrendSpotlight;
import com.metalancer.backend.users.domain.Creator;
import com.metalancer.backend.users.entity.CreatorEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    private String thumbnail;
    private String assetDetail;
    private String assetNotice;
    private String assetCopyRight;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "productsEntity")
    private ProductsAssetFileEntity productsAssetFileEntity;

    public void setActive() {
        active();
    }

    public void setDelete() {
        delete();
    }

    public void update(AssetUpdate dto) {
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.assetDetail = dto.getAssetDetail();
        this.assetNotice = dto.getAssetNotice();
        this.assetCopyRight = dto.getCopyRight();
    }

    public void update(AssetUpdateWithOutThumbnail dto) {
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.assetDetail = dto.getAssetDetail();
        this.assetNotice = dto.getAssetNotice();
        this.assetCopyRight = dto.getCopyRight();
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
            case DELETED ->
                throw new DataStatusException(PRODUCTS_STATUS_ERROR, ErrorCode.STATUS_DELETED);
        }
    }

    @Builder
    public ProductsEntity(CreatorEntity creatorEntity,
        String title, int price, String thumbnail, String assetDetail, String assetNotice,
        String assetCopyRight) {
        this.creatorEntity = creatorEntity;
        this.title = title;
        this.price = price;
        this.thumbnail = thumbnail;
        this.assetDetail = assetDetail;
        this.assetNotice = assetNotice;
        this.assetCopyRight = assetCopyRight;
        setSharedLink();
    }

    public ProductsDetail toProductsDetail(long taskCnt, double satisficationRate) {
        Creator creator = creatorEntity.toDomain();
        creator.setTaskCnt(taskCnt);
        creator.setSatisficationRate(satisficationRate);
        return ProductsDetail.builder().productsId(id).creator(
                creator)
            .sharedLink(sharedLink).title(title).price(price).salePrice(salePrice)
            .discount(discount).rate(rate).ratingCnt(ratingCnt)
            .assetDetail(assetDetail).assetNotice(assetNotice).assetCopyRight(assetCopyRight)
            .build();
    }

    public ProductsDetail toProductsDetail() {
        Creator creator = creatorEntity.toDomain();
        return ProductsDetail.builder().productsId(id).creator(
                creator)
            .sharedLink(sharedLink).title(title).price(price).salePrice(salePrice)
            .discount(discount).rate(rate).ratingCnt(ratingCnt)
            .assetDetail(assetDetail).assetNotice(assetNotice).assetCopyRight(assetCopyRight)
            .build();
    }

    public HotPickAsset toHotPickAsset() {
        return HotPickAsset.builder().productsId(id).title(title).price(price).thumbnail("")
            .build();
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
        return GenreGalaxy.builder().productsId(id).title(title).thumbnail(thumbnail).price(price)
            .build();
    }

    public TrendSpotlight toTrendSpotLight() {
        return TrendSpotlight.builder().productsId(id).title(title).thumbnail(thumbnail)
            .price(price).build();
    }

    public FilterAsset toFilterAsset() {
        return FilterAsset.builder().productsId(id).title(title).thumbnail(thumbnail).price(price)
            .build();
    }

    public ProductsList toAdminProductsList() {
        return ProductsList.builder().productsId(id).title(title).thumbnail(thumbnail).price(price)
            .dataStatus(getStatus()).createdAt(getCreatedAt()).updatedAt(getUpdatedAt())
            .build();
    }
}
