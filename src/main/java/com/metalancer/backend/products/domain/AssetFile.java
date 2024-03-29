package com.metalancer.backend.products.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AssetFile {

    private List<String> thumbnailUrlList;
    private List<String> viewUrlList;
    private String productionProgram;
    private String compatibleProgram;
    private double fileSize;
    private boolean animationStatus;
    private boolean riggingStatus;
    private String extensionList;
    // 수정일 기준!
    private String recentReleaseDate;
    private String support;
    private String copyRight;
    private String recentVersion;
    // 파일

    @Builder
    public AssetFile(
        String productionProgram, String compatibleProgram, double fileSize,
        boolean animationStatus, boolean riggingStatus, String extensionList,
        String recentReleaseDate,
        String support, String copyRight, String recentVersion) {
        this.productionProgram = productionProgram;
        this.compatibleProgram = compatibleProgram;
        this.fileSize = fileSize;
        this.animationStatus = animationStatus;
        this.riggingStatus = riggingStatus;
        this.extensionList = extensionList;
        this.recentReleaseDate = recentReleaseDate;
        this.support = support;
        this.copyRight = copyRight;
        this.recentVersion = recentVersion;
    }

    public void setThumbnailUrlList(List<String> thumbnailUrlList) {
        this.thumbnailUrlList = thumbnailUrlList;
    }

    public void setViewUrlList(List<String> viewUrlList) {
        this.viewUrlList = viewUrlList;
    }

}