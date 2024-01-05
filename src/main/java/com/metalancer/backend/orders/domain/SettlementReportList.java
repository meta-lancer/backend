package com.metalancer.backend.orders.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SettlementReportList {

    private final Long assetsId;
    private final String assetTitle;
    private final String assetExtension;
    private final Double assetsFileSize;
    private final boolean hasSettled;

    @Builder
    public SettlementReportList(Long assetsId, String assetTitle, String assetExtension,
        Double assetsFileSize, boolean hasSettled) {
        this.assetsId = assetsId;
        this.assetTitle = assetTitle;
        this.assetExtension = assetExtension;
        this.assetsFileSize = assetsFileSize;
        this.hasSettled = hasSettled;
    }
}