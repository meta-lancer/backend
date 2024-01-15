package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.OutLineOrdersStatList;
import com.metalancer.backend.admin.service.AdminOrdersStatService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
public class AdminOrdersStatController {

    private final AdminOrdersStatService adminOrdersStatService;

    @GetMapping("/list")
    public BaseResponse<List<OutLineOrdersStatList>> getOutlineOrdersStat(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("getOutlineOrdersStat API 호출 - {}", user.getUser().getName());
        return new BaseResponse<List<OutLineOrdersStatList>>(
            adminOrdersStatService.getOutlineOrdersStat());
    }

    @GetMapping("/list/all")
    public BaseResponse<List<OutLineOrdersStatList>> getAllOrdersStat(
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("getAllOrdersStat API 호출 - {}", user.getUser().getName());
        return new BaseResponse<List<OutLineOrdersStatList>>(
            adminOrdersStatService.getAllOrdersStat());
    }
}
