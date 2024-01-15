package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.AdminSettlementComplete;
import com.metalancer.backend.admin.domain.AdminSettlementIng;
import com.metalancer.backend.admin.domain.AdminSettlementReject;
import com.metalancer.backend.admin.domain.AdminSettlementRequest;
import com.metalancer.backend.admin.service.AdminSettlementService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/settlement")
public class AdminSettlementController {

    private final AdminSettlementService adminSettlementService;

    @GetMapping("/request")
    public BaseResponse<Page<AdminSettlementRequest>> getAdminSettlementRequestList(
        @AuthenticationPrincipal PrincipalDetails user, Pageable pageable) {
        log.info("정산요청 request 목록 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<Page<AdminSettlementRequest>>(
            adminSettlementService.getAdminSettlementRequestList(pageable));
    }

    @GetMapping("/ing")
    public BaseResponse<Page<AdminSettlementIng>> getAdminSettlementIngList(
        @AuthenticationPrincipal PrincipalDetails user, Pageable pageable) {
        log.info("정산요청 ing 목록 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<Page<AdminSettlementIng>>(
            adminSettlementService.getAdminSettlementIngList(pageable));
    }

    @GetMapping("/complete")
    public BaseResponse<Page<AdminSettlementComplete>> getAdminSettlementCompleteList(
        @AuthenticationPrincipal PrincipalDetails user, Pageable pageable) {
        log.info("정산요청 complete 목록 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<Page<AdminSettlementComplete>>(
            adminSettlementService.getAdminSettlementCompleteList(pageable));
    }

    @GetMapping("/reject")
    public BaseResponse<Page<AdminSettlementReject>> getAdminSettlementRejectList(
        @AuthenticationPrincipal PrincipalDetails user, Pageable pageable) {
        log.info("정산요청 request 목록 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<Page<AdminSettlementReject>>(
            adminSettlementService.getAdminSettlementRejectList(pageable));
    }


}