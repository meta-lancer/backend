package com.metalancer.backend.admin.domain;

import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class AdminSettlementComplete extends AdminSettlementCreatorAndPrice {

    String settlementDate;

    public AdminSettlementComplete(Creator creator,
        Long settlementRequestId, BigDecimal totalSalesPriceKRW,
        BigDecimal totalSalesPriceUSD, Integer totalSettlementPriceKRW,
        BigDecimal totalSettlementPriceUSD, BigDecimal totalServiceChargeKRW,
        BigDecimal totalServiceChargeUSD, BigDecimal totalFreeLancerChargeKRW,
        BigDecimal totalFreeLancerChargeUSD, BigDecimal totalPortoneChargeKRW,
        BigDecimal totalPortoneChargeUSD, String settlementDate) {
        super(creator, settlementRequestId, totalSalesPriceKRW, totalSalesPriceUSD,
            totalSettlementPriceKRW, totalSettlementPriceUSD, totalServiceChargeKRW,
            totalServiceChargeUSD, totalFreeLancerChargeKRW, totalFreeLancerChargeUSD,
            totalPortoneChargeKRW, totalPortoneChargeUSD);
        this.settlementDate = settlementDate;
    }
}