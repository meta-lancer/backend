package com.metalancer.backend.orders.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SettlementRecordList {

    private final Long settlementId;
    private final String requestDay;
    private final String currentSituation;
    private final String manager;
    private final String settlementStatus;

    @Builder
    public SettlementRecordList(Long settlementId, String requestDay, String currentSituation,
        String manager,
        String settlementStatus) {
        this.settlementId = settlementId;
        this.requestDay = requestDay;
        this.currentSituation = currentSituation;
        this.manager = manager;
        this.settlementStatus = settlementStatus;
    }
}