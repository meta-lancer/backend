package com.metalancer.backend.orders.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SettlementRequestInfo {

    private final Long settlementRequestId;
    private final BigDecimal totalSalesPriceKRW;
    private final BigDecimal totalSalesPriceUSD;
    private final BigDecimal totalSettlementPriceKRW;
    private final BigDecimal totalSettlementPriceUSD;
    private final BigDecimal totalServiceChargeKRW;
    private final BigDecimal totalServiceChargeUSD;
    private final BigDecimal totalFreeLancerChargeKRW;
    private final BigDecimal totalFreeLancerChargeUSD;
    private final BigDecimal totalPortoneChargeKRW;
    private final BigDecimal totalPortoneChargeUSD;

    @Builder
    public SettlementRequestInfo(Long settlementRequestId, BigDecimal totalSalesPriceKRW,
        BigDecimal totalSalesPriceUSD, BigDecimal totalSettlementPriceKRW,
        BigDecimal totalSettlementPriceUSD, BigDecimal totalServiceChargeKRW,
        BigDecimal totalServiceChargeUSD, BigDecimal totalFreeLancerChargeKRW,
        BigDecimal totalFreeLancerChargeUSD, BigDecimal totalPortoneChargeKRW,
        BigDecimal totalPortoneChargeUSD) {
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