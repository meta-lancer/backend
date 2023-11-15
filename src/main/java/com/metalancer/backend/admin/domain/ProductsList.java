package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.domain.Asset;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductsList extends Asset {

    private boolean assetFileSuccess;
    private DataStatus dataStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ProductsList(Long productsId, String title, String thumbnail, Integer price,
        DataStatus dataStatus, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        super(productsId, title, thumbnail, price);
        this.dataStatus = dataStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setAssetFileSuccess(boolean assetFileSuccess) {
        this.assetFileSuccess = assetFileSuccess;
    }
}