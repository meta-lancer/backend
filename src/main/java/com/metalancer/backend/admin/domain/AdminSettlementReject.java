package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class AdminSettlementReject extends AdminSettlementCreatorAndPrice {

    String rejectDate;

    public AdminSettlementReject(Creator creator,
        Long settlementRequestId, BigDecimal totalSalesPriceKRW,
        BigDecimal totalSalesPriceUSD, Integer totalSettlementPriceKRW,
        BigDecimal totalSettlementPriceUSD, BigDecimal totalServiceChargeKRW,
        BigDecimal totalServiceChargeUSD, BigDecimal totalFreeLancerChargeKRW,
        BigDecimal totalFreeLancerChargeUSD, BigDecimal totalPortoneChargeKRW,
        BigDecimal totalPortoneChargeUSD, String rejectDate, SettlementStatus settlementStatus,
        Integer settlementSalesCnt) {
        super(creator, settlementRequestId, totalSalesPriceKRW, totalSalesPriceUSD,
            totalSettlementPriceKRW, totalSettlementPriceUSD, totalServiceChargeKRW,
            totalServiceChargeUSD, totalFreeLancerChargeKRW, totalFreeLancerChargeUSD,
            totalPortoneChargeKRW, totalPortoneChargeUSD, settlementStatus, settlementSalesCnt);
        this.rejectDate = rejectDate;
    }
}