package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminSettlementCreatorAndPrice {

    private final Creator creator;
    private final Long settlementRequestId;
    private final BigDecimal totalSalesPriceKRW;
    private final BigDecimal totalSalesPriceUSD;
    private final Integer totalSettlementPriceKRW;
    private final BigDecimal totalSettlementPriceUSD;
    private final BigDecimal totalServiceChargeKRW;
    private final BigDecimal totalServiceChargeUSD;
    private final BigDecimal totalFreeLancerChargeKRW;
    private final BigDecimal totalFreeLancerChargeUSD;
    private final BigDecimal totalPortoneChargeKRW;
    private final BigDecimal totalPortoneChargeUSD;
    private final SettlementStatus settlementStatus;
    private final Integer settlementSalesCnt;
    // 관리자 정보
    // 처리 정보

    @Builder
    public AdminSettlementCreatorAndPrice(Creator creator, Long settlementRequestId,
        BigDecimal totalSalesPriceKRW,
        BigDecimal totalSalesPriceUSD, Integer totalSettlementPriceKRW,
        BigDecimal totalSettlementPriceUSD, BigDecimal totalServiceChargeKRW,
        BigDecimal totalServiceChargeUSD, BigDecimal totalFreeLancerChargeKRW,
        BigDecimal totalFreeLancerChargeUSD, BigDecimal totalPortoneChargeKRW,
        BigDecimal totalPortoneChargeUSD, SettlementStatus settlementStatus,
        Integer settlementSalesCnt) {
        this.creator = creator;
        this.settlementRequestId = settlementRequestId;
        this.totalSalesPriceKRW = totalSalesPriceKRW;
        this.totalSalesPriceUSD = totalSalesPriceUSD;
        this.totalSettlementPriceKRW = totalSettlementPriceKRW;
        this.totalSettlementPriceUSD = totalSettlementPriceUSD;
        this.totalServiceChargeKRW = totalServiceChargeKRW;
        this.totalServiceChargeUSD = totalServiceChargeUSD;
        this.totalFreeLancerChargeKRW = totalFreeLancerChargeKRW;
        this.totalFreeLancerChargeUSD = totalFreeLancerChargeUSD;
        this.totalPortoneChargeKRW = totalPortoneChargeKRW;
        this.totalPortoneChargeUSD = totalPortoneChargeUSD;
        this.settlementStatus = settlementStatus;
        this.settlementSalesCnt = settlementSalesCnt;
    }
}