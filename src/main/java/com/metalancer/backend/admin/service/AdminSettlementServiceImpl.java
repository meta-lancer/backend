package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.AdminSettlementComplete;
import com.metalancer.backend.admin.domain.AdminSettlementIng;
import com.metalancer.backend.admin.domain.AdminSettlementReject;
import com.metalancer.backend.admin.domain.AdminSettlementRequest;
import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.creators.entity.SettlementEntity;
import com.metalancer.backend.creators.repository.SettlementRepository;
import com.metalancer.backend.users.domain.Creator;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminSettlementServiceImpl implements AdminSettlementService {

    private final SettlementRepository settlementRepository;

    @Override
    public Page<AdminSettlementRequest> getAdminSettlementRequestList(
        Pageable pageable) {
        Page<SettlementEntity> settlementEntities = settlementRepository.findAllBySettlementStatus(
            SettlementStatus.REQUEST, pageable);
        Page<AdminSettlementRequest> response = settlementEntities.map(
            this::convertToAdminSettlementRequest);
        return response;
    }

    @Override
    public Page<AdminSettlementIng> getAdminSettlementIngList(Pageable pageable) {
        Page<SettlementEntity> settlementEntities = settlementRepository.findAllBySettlementStatus(
            SettlementStatus.ING, pageable);
        Page<AdminSettlementIng> response = settlementEntities.map(
            this::convertToAdminSettlementIng);
        return response;
    }

    @Override
    public Page<AdminSettlementComplete> getAdminSettlementCompleteList(
        Pageable pageable) {
        Page<SettlementEntity> settlementEntities = settlementRepository.findAllBySettlementStatus(
            SettlementStatus.COMPLETE, pageable);
        Page<AdminSettlementComplete> response = settlementEntities.map(
            this::convertToAdminSettlementComplete);
        return response;
    }

    @Override
    public Page<AdminSettlementReject> getAdminSettlementRejectList(
        Pageable pageable) {
        Page<SettlementEntity> settlementEntities = settlementRepository.findAllBySettlementStatus(
            SettlementStatus.REJECT, pageable);
        Page<AdminSettlementReject> response = settlementEntities.map(
            this::convertToAdminSettlementReject);
        return response;
    }

    private AdminSettlementIng convertToAdminSettlementIng(
        SettlementEntity settlementEntity) {
        Creator creator = settlementEntity.getCreatorEntity().toDomain();

        return AdminSettlementIng.builder()
            .creator(creator)
            .settlementRequestId(settlementEntity.getId())
            .totalSalesPriceKRW(settlementEntity.getTotalAmountKRW())
            .totalSalesPriceUSD(settlementEntity.getTotalAmountUSD())
            .totalSettlementPriceKRW(BigDecimal.valueOf(settlementEntity.getSettlementAmountKRW()))
            .totalSettlementPriceUSD(settlementEntity.getSettlementAmountUSD())
            .totalServiceChargeKRW(settlementEntity.getServiceChargeAmountKRW())
            .totalServiceChargeUSD(settlementEntity.getServiceChargeAmountUSD())
            .totalFreeLancerChargeKRW(settlementEntity.getFreelancerChargeAmountKRW())
            .totalFreeLancerChargeUSD(settlementEntity.getFreelancerChargeAmountUSD())
            .totalPortoneChargeKRW(settlementEntity.getPortoneChargeAmountKRW())
            .totalPortoneChargeUSD(settlementEntity.getPortoneChargeAmountUSD())
            .processDate(Time.convertDateToFullString(settlementEntity.getProcessDate()))
            .build();
    }

    private AdminSettlementComplete convertToAdminSettlementComplete(
        SettlementEntity settlementEntity) {
        Creator creator = settlementEntity.getCreatorEntity().toDomain();

        return AdminSettlementComplete.builder()
            .creator(creator)
            .settlementRequestId(settlementEntity.getId())
            .totalSalesPriceKRW(settlementEntity.getTotalAmountKRW())
            .totalSalesPriceUSD(settlementEntity.getTotalAmountUSD())
            .totalSettlementPriceKRW(BigDecimal.valueOf(settlementEntity.getSettlementAmountKRW()))
            .totalSettlementPriceUSD(settlementEntity.getSettlementAmountUSD())
            .totalServiceChargeKRW(settlementEntity.getServiceChargeAmountKRW())
            .totalServiceChargeUSD(settlementEntity.getServiceChargeAmountUSD())
            .totalFreeLancerChargeKRW(settlementEntity.getFreelancerChargeAmountKRW())
            .totalFreeLancerChargeUSD(settlementEntity.getFreelancerChargeAmountUSD())
            .totalPortoneChargeKRW(settlementEntity.getPortoneChargeAmountKRW())
            .totalPortoneChargeUSD(settlementEntity.getPortoneChargeAmountUSD())
            .settlementDate(Time.convertDateToFullString(settlementEntity.getSettlementDate()))
            .build();
    }

    private AdminSettlementRequest convertToAdminSettlementRequest(
        SettlementEntity settlementEntity) {
        Creator creator = settlementEntity.getCreatorEntity().toDomain();

        return AdminSettlementRequest.builder()
            .creator(creator)
            .settlementRequestId(settlementEntity.getId())
            .totalSalesPriceKRW(settlementEntity.getTotalAmountKRW())
            .totalSalesPriceUSD(settlementEntity.getTotalAmountUSD())
            .totalSettlementPriceKRW(BigDecimal.valueOf(settlementEntity.getSettlementAmountKRW()))
            .totalSettlementPriceUSD(settlementEntity.getSettlementAmountUSD())
            .totalServiceChargeKRW(settlementEntity.getServiceChargeAmountKRW())
            .totalServiceChargeUSD(settlementEntity.getServiceChargeAmountUSD())
            .totalFreeLancerChargeKRW(settlementEntity.getFreelancerChargeAmountKRW())
            .totalFreeLancerChargeUSD(settlementEntity.getFreelancerChargeAmountUSD())
            .totalPortoneChargeKRW(settlementEntity.getPortoneChargeAmountKRW())
            .totalPortoneChargeUSD(settlementEntity.getPortoneChargeAmountUSD())
            .requestDate(Time.convertDateToFullString(settlementEntity.getRequestDate()))
            .build();
    }

    private AdminSettlementReject convertToAdminSettlementReject(
        SettlementEntity settlementEntity) {
        Creator creator = settlementEntity.getCreatorEntity().toDomain();

        return AdminSettlementReject.builder()
            .creator(creator)
            .settlementRequestId(settlementEntity.getId())
            .totalSalesPriceKRW(settlementEntity.getTotalAmountKRW())
            .totalSalesPriceUSD(settlementEntity.getTotalAmountUSD())
            .totalSettlementPriceKRW(BigDecimal.valueOf(settlementEntity.getSettlementAmountKRW()))
            .totalSettlementPriceUSD(settlementEntity.getSettlementAmountUSD())
            .totalServiceChargeKRW(settlementEntity.getServiceChargeAmountKRW())
            .totalServiceChargeUSD(settlementEntity.getServiceChargeAmountUSD())
            .totalFreeLancerChargeKRW(settlementEntity.getFreelancerChargeAmountKRW())
            .totalFreeLancerChargeUSD(settlementEntity.getFreelancerChargeAmountUSD())
            .totalPortoneChargeKRW(settlementEntity.getPortoneChargeAmountKRW())
            .totalPortoneChargeUSD(settlementEntity.getPortoneChargeAmountUSD())
            .rejectDate(Time.convertDateToFullString(settlementEntity.getRejectDate()))
            .build();
    }
}