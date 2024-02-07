package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.Charge;
import com.metalancer.backend.admin.dto.AdminServiceDTO;
import com.metalancer.backend.admin.service.AdminServiceService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.AuthUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/service")
public class AdminServiceController {

    private final AdminServiceService adminServiceService;

    @GetMapping("/charge")
    public BaseResponse<List<Charge>> getAdminServiceChargeList(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("서비스 수수료 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<List<Charge>>(
            adminServiceService.getAdminServiceChargeList());
    }

    @PatchMapping("/charge")
    public BaseResponse<Boolean> updateAdminServiceCharge(
        @RequestBody AdminServiceDTO.UpdateServiceCharge dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("서비스 수수료 API 수정 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        log.info("서비스 수수료 API 수정 dto - {}", dto);
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<Boolean>(
            adminServiceService.updateAdminServiceCharge(user, dto));
    }

    @GetMapping("/charge/portone")
    public BaseResponse<List<Charge>> getAdminPortoneChargeList(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("포트원 수수료 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<List<Charge>>(
            adminServiceService.getAdminPortoneChargeList());
    }

    @PatchMapping("/charge/portone")
    public BaseResponse<Boolean> updateAdminPortoneCharge(
        @RequestBody AdminServiceDTO.UpdatePortoneCharge dto,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("포트원 수수료 API 수정 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        log.info("포트원 수수료 API 수정 dto - {}", dto);
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<Boolean>(
            adminServiceService.updateAdminPortoneCharge(user, dto));
    }
}
