package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.AdminSettlementComplete;
import com.metalancer.backend.admin.domain.AdminSettlementCreatorAndPrice;
import com.metalancer.backend.admin.domain.AdminSettlementIng;
import com.metalancer.backend.admin.domain.AdminSettlementReject;
import com.metalancer.backend.admin.domain.AdminSettlementRequest;
import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.creators.entity.SettlementEntity;
import com.metalancer.backend.creators.repository.SettlementProductsRepository;
import com.metalancer.backend.creators.repository.SettlementRepository;
import com.metalancer.backend.users.domain.Creator;
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
    private final SettlementProductsRepository settlementProductsRepository;

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

    private AdminSettlementCreatorAndPrice convertToAdminSettlementBase(
        SettlementEntity settlementEntity) {
        Creator creator = settlementEntity.getCreatorEntity().toDomain();
        return AdminSettlementCreatorAndPrice.builder()
            .creator(creator)
            .settlementRequestId(settlementEntity.getId())
            .totalSalesPriceKRW(settlementEntity.getTotalAmountKRW())
            .totalSalesPriceUSD(settlementEntity.getTotalAmountUSD())
            .totalSettlementPriceKRW(settlementEntity.getSettlementAmountKRW())
            .totalSettlementPriceUSD(settlementEntity.getSettlementAmountUSD())
            .totalServiceChargeKRW(settlementEntity.getServiceChargeAmountKRW())
            .totalServiceChargeUSD(settlementEntity.getServiceChargeAmountUSD())
            .totalFreeLancerChargeKRW(settlementEntity.getFreelancerChargeAmountKRW())
            .totalFreeLancerChargeUSD(settlementEntity.getFreelancerChargeAmountUSD())
            .totalPortoneChargeKRW(settlementEntity.getPortoneChargeAmountKRW())
            .totalPortoneChargeUSD(settlementEntity.getPortoneChargeAmountUSD())
            .build();
    }

    private AdminSettlementIng convertToAdminSettlementIng(
        SettlementEntity settlementEntity) {
        AdminSettlementCreatorAndPrice base = convertToAdminSettlementBase(settlementEntity);
        Integer settlementSalesCnt = settlementProductsRepository.countAllBySettlement(
            settlementEntity);
        return new AdminSettlementIng(
            base.getCreator(),
            base.getSettlementRequestId(),
            base.getTotalSalesPriceKRW(),
            base.getTotalSalesPriceUSD(),
            base.getTotalSettlementPriceKRW(),
            base.getTotalSettlementPriceUSD(),
            base.getTotalServiceChargeKRW(),
            base.getTotalServiceChargeUSD(),
            base.getTotalFreeLancerChargeKRW(),
            base.getTotalFreeLancerChargeUSD(),
            base.getTotalPortoneChargeKRW(),
            base.getTotalPortoneChargeUSD(),
            Time.convertDateToFullString(settlementEntity.getProcessDate()),
            settlementEntity.getSettlementStatus(),
            settlementSalesCnt
        );
    }

    private AdminSettlementComplete convertToAdminSettlementComplete(
        SettlementEntity settlementEntity) {
        AdminSettlementCreatorAndPrice base = convertToAdminSettlementBase(settlementEntity);
        Integer settlementSalesCnt = settlementProductsRepository.countAllBySettlement(
            settlementEntity);
        return new AdminSettlementComplete(
            base.getCreator(),
            base.getSettlementRequestId(),
            base.getTotalSalesPriceKRW(),
            base.getTotalSalesPriceUSD(),
            base.getTotalSettlementPriceKRW(),
            base.getTotalSettlementPriceUSD(),
            base.getTotalServiceChargeKRW(),
            base.getTotalServiceChargeUSD(),
            base.getTotalFreeLancerChargeKRW(),
            base.getTotalFreeLancerChargeUSD(),
            base.getTotalPortoneChargeKRW(),
            base.getTotalPortoneChargeUSD(),
            Time.convertDateToFullString(settlementEntity.getSettlementDate()),
            settlementEntity.getSettlementStatus(),
            settlementSalesCnt
        );
    }

    private AdminSettlementRequest convertToAdminSettlementRequest(
        SettlementEntity settlementEntity) {
        AdminSettlementCreatorAndPrice base = convertToAdminSettlementBase(settlementEntity);
        Integer settlementSalesCnt = settlementProductsRepository.countAllBySettlement(
            settlementEntity);
        return new AdminSettlementRequest(
            base.getCreator(),
            base.getSettlementRequestId(),
            base.getTotalSalesPriceKRW(),
            base.getTotalSalesPriceUSD(),
            base.getTotalSettlementPriceKRW(),
            base.getTotalSettlementPriceUSD(),
            base.getTotalServiceChargeKRW(),
            base.getTotalServiceChargeUSD(),
            base.getTotalFreeLancerChargeKRW(),
            base.getTotalFreeLancerChargeUSD(),
            base.getTotalPortoneChargeKRW(),
            base.getTotalPortoneChargeUSD(),
            Time.convertDateToFullString(settlementEntity.getRequestDate()),
            settlementEntity.getSettlementStatus(),
            settlementSalesCnt
        );
    }

    private AdminSettlementReject convertToAdminSettlementReject(
        SettlementEntity settlementEntity) {
        AdminSettlementCreatorAndPrice base = convertToAdminSettlementBase(settlementEntity);
        Integer settlementSalesCnt = settlementProductsRepository.countAllBySettlement(
            settlementEntity);
        return new AdminSettlementReject(
            base.getCreator(),
            base.getSettlementRequestId(),
            base.getTotalSalesPriceKRW(),
            base.getTotalSalesPriceUSD(),
            base.getTotalSettlementPriceKRW(),
            base.getTotalSettlementPriceUSD(),
            base.getTotalServiceChargeKRW(),
            base.getTotalServiceChargeUSD(),
            base.getTotalFreeLancerChargeKRW(),
            base.getTotalFreeLancerChargeUSD(),
            base.getTotalPortoneChargeKRW(),
            base.getTotalPortoneChargeUSD(),
            Time.convertDateToFullString(settlementEntity.getRejectDate()),
            settlementEntity.getSettlementStatus(),
            settlementSalesCnt
        );
    }
}