package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.creators.domain.PaymentInfoManagement;
import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class AdminSettlementRequest extends AdminSettlementCreatorAndPrice {

    private String requestDate;

    public AdminSettlementRequest(Creator creator,
        Long settlementRequestId, BigDecimal totalSalesPriceKRW,
        BigDecimal totalSalesPriceUSD, Integer totalSettlementPriceKRW,
        BigDecimal totalSettlementPriceUSD, BigDecimal totalServiceChargeKRW,
        BigDecimal totalServiceChargeUSD, BigDecimal totalFreeLancerChargeKRW,
        BigDecimal totalFreeLancerChargeUSD, BigDecimal totalPortoneChargeKRW,
        BigDecimal totalPortoneChargeUSD, String requestDate, SettlementStatus settlementStatus,
        Integer settlementSalesCnt, PaymentInfoManagement paymentInfoManagement,
        AdminManager adminManager) {
        super(creator, settlementRequestId, totalSalesPriceKRW, totalSalesPriceUSD,
            totalSettlementPriceKRW, totalSettlementPriceUSD, totalServiceChargeKRW,
            totalServiceChargeUSD, totalFreeLancerChargeKRW, totalFreeLancerChargeUSD,
            totalPortoneChargeKRW, totalPortoneChargeUSD, settlementStatus, settlementSalesCnt,
            paymentInfoManagement, adminManager);
        this.requestDate = requestDate;
    }
}