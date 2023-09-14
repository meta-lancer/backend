package com.metalancer.backend.products.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AssetFile {

    private List<String> thumbnailUrlList;
    private List<String> viewUrlList;
    private String zipFileUrl;
    private String productionProgram;
    private String compatibleProgram;
    private double fileSize;
    private boolean animationStatus;
    private boolean riggingStatus;
    private String extensionList;
    private String recentReleaseDate;
    private String support;
    private String copyRight;
    private String recentVersion;
    // 파일

    @Builder
    public AssetFile(List<String> thumbnailUrlList, List<String> viewUrlList, String zipFileUrl,
        String productionProgram, String compatibleProgram, double fileSize,
        boolean animationStatus, boolean riggingStatus, String extensionList,
        String recentReleaseDate,
        String support, String copyRight, String recentVersion) {
        this.thumbnailUrlList = thumbnailUrlList;
        this.viewUrlList = viewUrlList;
        this.zipFileUrl = zipFileUrl;
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
}