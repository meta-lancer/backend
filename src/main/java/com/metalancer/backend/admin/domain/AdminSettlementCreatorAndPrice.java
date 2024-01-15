package com.metalancer.backend.admin.domain;

import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminSettlementCreatorAndPrice {

    private Creator creator;
    private Long settlementRequestId;
    private BigDecimal totalSalesPriceKRW;
    private BigDecimal totalSalesPriceUSD;
    private Integer totalSettlementPriceKRW;
    private BigDecimal totalSettlementPriceUSD;
    private BigDecimal totalServiceChargeKRW;
    private BigDecimal totalServiceChargeUSD;
    private BigDecimal totalFreeLancerChargeKRW;
    private BigDecimal totalFreeLancerChargeUSD;
    private BigDecimal totalPortoneChargeKRW;
    private BigDecimal totalPortoneChargeUSD;

    @Builder
    public AdminSettlementCreatorAndPrice(Creator creator, Long settlementRequestId,
        BigDecimal totalSalesPriceKRW,
        BigDecimal totalSalesPriceUSD, Integer totalSettlementPriceKRW,
        BigDecimal totalSettlementPriceUSD, BigDecimal totalServiceChargeKRW,
        BigDecimal totalServiceChargeUSD, BigDecimal totalFreeLancerChargeKRW,
        BigDecimal totalFreeLancerChargeUSD, BigDecimal totalPortoneChargeKRW,
        BigDecimal totalPortoneChargeUSD) {
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
    }
}