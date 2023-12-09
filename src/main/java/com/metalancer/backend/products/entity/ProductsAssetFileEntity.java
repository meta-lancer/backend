package com.metalancer.backend.products.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.metalancer.backend.common.BaseTimeEntity;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetUpdate;
import com.metalancer.backend.creators.dto.CreatorRequestDTO.AssetUpdateWithOutThumbnail;
import com.metalancer.backend.products.domain.AssetFile;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "products_asset_file")
@ToString
public class ProductsAssetFileEntity extends BaseTimeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 748309177241999615L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "products_asset_file_id", nullable = false)
    private Long id;


    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "products_id", nullable = false)
    private ProductsEntity productsEntity;

    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private String fileName;
    private Boolean success = false;
    @Schema(description = "제작 프로그램", example = "Cinema 4D")
    private String productionProgram;
    @Schema(description = "호환 프로그램", example = "C4D, 블렌더, 유니티")
    private String compatibleProgram;
    @Schema(description = "파일크기(MB 기준)", example = "973.4")
    private Double fileSize;
    @Schema(description = "애니메이션", example = "")
    private Boolean animation;
    @Schema(description = "리깅", example = "")
    private Boolean rigging;
    @Schema(description = "확장", example = "fbx, stl, obj, c4d, 3ds")
    private String extList;
    @Schema(description = "지원", example = "웹사이트 방문")
    private String support;
    @Schema(description = "저작권", example = "비독점적 사용권 상업적/비상업적 사용가능")
    private String copyRight;
    @Schema(description = "최신 버전", example = "1.12")
    private String recentVersion;

    @Builder
    public ProductsAssetFileEntity(ProductsEntity productsEntity, String url, String fileName,
        String productionProgram, String compatibleProgram, Double fileSize, Boolean animation,
        Boolean rigging, String extList, String support, String copyRight, String recentVersion) {
        this.productsEntity = productsEntity;
        this.url = url;
        this.fileName = fileName;
        this.productionProgram = productionProgram;
        this.compatibleProgram = compatibleProgram;
        this.fileSize = fileSize;
        this.animation = animation;
        this.rigging = rigging;
        this.extList = extList;
        this.support = support;
        this.copyRight = copyRight;
        this.recentVersion = recentVersion;
    }

    public void success() {
        this.success = true;
    }

    public void update(ProductsEntity productsEntity, AssetUpdate dto) {
        this.productsEntity = productsEntity;
        this.productionProgram = String.join(", ", dto.getProductionProgram());
        this.compatibleProgram = String.join(", ", dto.getCompatibleProgram());
        this.fileSize = dto.getFileSize();
        this.animation = dto.getAnimation();
        this.rigging = dto.getRigging();
        this.extList = String.join(", ", dto.getExtList());
        this.support = dto.getSupport();
        this.copyRight = dto.getCopyRight();
        this.recentVersion = dto.getRecentVersion();
    }

    public void update(ProductsEntity productsEntity, AssetUpdateWithOutThumbnail dto) {
        this.productsEntity = productsEntity;
        this.productionProgram = String.join(", ", dto.getProductionProgram());
        this.compatibleProgram = String.join(", ", dto.getCompatibleProgram());
        this.fileSize = dto.getFileSize();
        this.animation = dto.getAnimation();
        this.rigging = dto.getRigging();
        this.extList = String.join(", ", dto.getExtList());
        this.support = dto.getSupport();
        this.copyRight = dto.getCopyRight();
        this.recentVersion = dto.getRecentVersion();
    }

    public CreatorAssetList toCreatorAssetList() {
        return CreatorAssetList.builder().thumbnail(productsEntity.getThumbnail()).title(
                productsEntity.getTitle()).price(productsEntity.getPrice()).productsId(
                productsEntity.getId())
            .build();
    }

    public AssetFile toAssetFile() {
        return AssetFile.builder()
            .productionProgram(productionProgram).compatibleProgram(compatibleProgram)
            .fileSize(fileSize)
            .animationStatus(animation).riggingStatus(rigging)
            .extensionList(extList).recentReleaseDate(Time.convertDateToString(getUpdatedAt()))
            .copyRight(copyRight).recentVersion(recentVersion)
            .support(support)
            .build();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
