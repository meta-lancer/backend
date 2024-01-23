package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.AdminManager;
import com.metalancer.backend.admin.domain.AdminSettlementComplete;
import com.metalancer.backend.admin.domain.AdminSettlementCreatorAndPrice;
import com.metalancer.backend.admin.domain.AdminSettlementIng;
import com.metalancer.backend.admin.domain.AdminSettlementReject;
import com.metalancer.backend.admin.domain.AdminSettlementRequest;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.creators.domain.PaymentInfoManagement;
import com.metalancer.backend.creators.entity.PaymentInfoManagementEntity;
import com.metalancer.backend.creators.entity.SettlementEntity;
import com.metalancer.backend.creators.repository.PaymentInfoManagementRepository;
import com.metalancer.backend.creators.repository.SettlementProductsRepository;
import com.metalancer.backend.creators.repository.SettlementRepository;
import com.metalancer.backend.users.domain.Creator;
import com.metalancer.backend.users.entity.CreatorEntity;
import com.metalancer.backend.users.entity.User;
import com.metalancer.backend.users.repository.UserRepository;
import java.util.Optional;
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
    private final PaymentInfoManagementRepository paymentInfoManagementRepository;
    private final UserRepository userRepository;

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
        String managerName =
            settlementEntity.getManager() != null ? settlementEntity.getManager() : "";
        AdminManager adminManager = userRepository.findByNameAndRoleAndStatus(
            managerName, Role.ROLE_ADMIN, DataStatus.ACTIVE).map(User::toAdminManager).orElse(null);
        AdminSettlementCreatorAndPrice base = convertToAdminSettlementBase(settlementEntity);
        Integer settlementSalesCnt = settlementProductsRepository.countAllBySettlement(
            settlementEntity);
        CreatorEntity creatorEntity = settlementEntity.getCreatorEntity();
        Optional<PaymentInfoManagementEntity> optionalPaymentInfoManagement = paymentInfoManagementRepository.findByCreatorEntity(
            creatorEntity);
        PaymentInfoManagement paymentInfoManagement =
            optionalPaymentInfoManagement.map(PaymentInfoManagementEntity::toPaymentInfoManagement)
                .orElse(null);
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
            Time.convertDateToFullString(settlementEntity.getRequestDate()),
            settlementEntity.getSettlementStatus(),
            settlementSalesCnt,
            paymentInfoManagement,
            adminManager,
            settlementEntity.getReferenceMemo(),
            settlementEntity.getReferenceFile()
        );
    }

    private AdminSettlementComplete convertToAdminSettlementComplete(
        SettlementEntity settlementEntity) {
        String managerName =
            settlementEntity.getManager() != null ? settlementEntity.getManager() : "";
        AdminManager adminManager = userRepository.findByNameAndRoleAndStatus(
            managerName, Role.ROLE_ADMIN, DataStatus.ACTIVE).map(User::toAdminManager).orElse(null);
        AdminSettlementCreatorAndPrice base = convertToAdminSettlementBase(settlementEntity);
        Integer settlementSalesCnt = settlementProductsRepository.countAllBySettlement(
            settlementEntity);
        CreatorEntity creatorEntity = settlementEntity.getCreatorEntity();
        Optional<PaymentInfoManagementEntity> optionalPaymentInfoManagement = paymentInfoManagementRepository.findByCreatorEntity(
            creatorEntity);
        PaymentInfoManagement paymentInfoManagement =
            optionalPaymentInfoManagement.map(PaymentInfoManagementEntity::toPaymentInfoManagement)
                .orElse(null);
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
            Time.convertDateToFullString(settlementEntity.getRequestDate()),
            Time.convertDateToFullString(settlementEntity.getProcessDate()),
            Time.convertDateToFullString(settlementEntity.getSettlementDate()),
            settlementEntity.getSettlementStatus(),
            settlementSalesCnt,
            paymentInfoManagement,
            adminManager,
            settlementEntity.getReferenceMemo(),
            settlementEntity.getReferenceFile()
        );
    }

    private AdminSettlementRequest convertToAdminSettlementRequest(
        SettlementEntity settlementEntity) {
        String managerName =
            settlementEntity.getManager() != null ? settlementEntity.getManager() : "";
        AdminManager adminManager = userRepository.findByNameAndRoleAndStatus(
            managerName, Role.ROLE_ADMIN, DataStatus.ACTIVE).map(User::toAdminManager).orElse(null);
        AdminSettlementCreatorAndPrice base = convertToAdminSettlementBase(settlementEntity);
        Integer settlementSalesCnt = settlementProductsRepository.countAllBySettlement(
            settlementEntity);
        CreatorEntity creatorEntity = settlementEntity.getCreatorEntity();
        Optional<PaymentInfoManagementEntity> optionalPaymentInfoManagement = paymentInfoManagementRepository.findByCreatorEntity(
            creatorEntity);
        PaymentInfoManagement paymentInfoManagement =
            optionalPaymentInfoManagement.map(PaymentInfoManagementEntity::toPaymentInfoManagement)
                .orElse(null);
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
            settlementSalesCnt,
            paymentInfoManagement,
            adminManager,
            settlementEntity.getReferenceMemo(),
            settlementEntity.getReferenceFile()
        );
    }

    private AdminSettlementReject convertToAdminSettlementReject(
        SettlementEntity settlementEntity) {
        String managerName =
            settlementEntity.getManager() != null ? settlementEntity.getManager() : "";
        AdminManager adminManager = userRepository.findByNameAndRoleAndStatus(
            managerName, Role.ROLE_ADMIN, DataStatus.ACTIVE).map(User::toAdminManager).orElse(null);
        AdminSettlementCreatorAndPrice base = convertToAdminSettlementBase(settlementEntity);
        Integer settlementSalesCnt = settlementProductsRepository.countAllBySettlement(
            settlementEntity);
        CreatorEntity creatorEntity = settlementEntity.getCreatorEntity();
        Optional<PaymentInfoManagementEntity> optionalPaymentInfoManagement = paymentInfoManagementRepository.findByCreatorEntity(
            creatorEntity);
        PaymentInfoManagement paymentInfoManagement =
            optionalPaymentInfoManagement.map(PaymentInfoManagementEntity::toPaymentInfoManagement)
                .orElse(null);
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
            Time.convertDateToFullString(settlementEntity.getRequestDate()),
            Time.convertDateToFullString(settlementEntity.getProcessDate()),
            Time.convertDateToFullString(settlementEntity.getRejectDate()),
            settlementEntity.getSettlementStatus(),
            settlementSalesCnt,
            paymentInfoManagement,
            adminManager,
            settlementEntity.getReferenceMemo(),
            settlementEntity.getReferenceFile()
        );
    }

    @Override
    public Boolean addManagerOfSettlement(PrincipalDetails user, Long settlementRequestId) {
        User adminUser = user.getUser();
        SettlementEntity settlementEntity = settlementRepository.findById(settlementRequestId)
            .orElseThrow(
                () -> new NotFoundException("정산요청", ErrorCode.NOT_FOUND)
            );
        settlementEntity.addManage(adminUser.getName());
        return settlementEntity.getManager().equals(adminUser.getName());
    }
}