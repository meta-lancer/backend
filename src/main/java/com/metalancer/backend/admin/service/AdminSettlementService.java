package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.domain.AdminSettlementComplete;
import com.metalancer.backend.admin.domain.AdminSettlementIng;
import com.metalancer.backend.admin.domain.AdminSettlementReject;
import com.metalancer.backend.admin.domain.AdminSettlementRequest;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSettlementService {

    Page<AdminSettlementRequest> getAdminSettlementRequestList(Pageable pageable);

    Page<AdminSettlementIng> getAdminSettlementIngList(Pageable pageable);

    Page<AdminSettlementComplete> getAdminSettlementCompleteList(Pageable pageable);

    Page<AdminSettlementReject> getAdminSettlementRejectList(Pageable pageable);

    Boolean addManagerOfSettlement(PrincipalDetails user, Long settlementRequestId);
}