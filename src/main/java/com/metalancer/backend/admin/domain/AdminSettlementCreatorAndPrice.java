package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.creators.domain.PaymentInfoManagement;
import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminSettlementCreatorAndPrice {

    private final Creator creator;
    private final PaymentInfoManagement paymentInfoManagement;
    //    private final AdminManager adminManager;
    private Long adminMemberId;
    private String adminName;
    private String adminNickname;
    private String adminMobile;
    private String adminEmail;
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
    private String referenceMemo;
    private String referenceFile;

    @Builder
    public AdminSettlementCreatorAndPrice(Creator creator, Long settlementRequestId,
        BigDecimal totalSalesPriceKRW,
        BigDecimal totalSalesPriceUSD, Integer totalSettlementPriceKRW,
        BigDecimal totalSettlementPriceUSD, BigDecimal totalServiceChargeKRW,
        BigDecimal totalServiceChargeUSD, BigDecimal totalFreeLancerChargeKRW,
        BigDecimal totalFreeLancerChargeUSD, BigDecimal totalPortoneChargeKRW,
        BigDecimal totalPortoneChargeUSD, SettlementStatus settlementStatus,
        Integer settlementSalesCnt, PaymentInfoManagement paymentInfoManagement,
        AdminManager adminManager, String referenceMemo, String referenceFile) {
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
        this.paymentInfoManagement = paymentInfoManagement;
        this.adminMemberId = adminManager != null ? adminManager.getAdminMemberId() : null;
        this.adminName = adminManager != null ? adminManager.getAdminName() : null;
        this.adminNickname = adminManager != null ? adminManager.getAdminNickname() : null;
        this.adminMobile = adminManager != null ? adminManager.getAdminMobile() : null;
        this.adminEmail = adminManager != null ? adminManager.getAdminEmail() : null;
        this.referenceMemo = referenceMemo;
        this.referenceFile = referenceFile;
    }
}