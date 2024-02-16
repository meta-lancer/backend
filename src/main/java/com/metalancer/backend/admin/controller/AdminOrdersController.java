package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.dto.AdminOrderDTO;
import com.metalancer.backend.admin.service.AdminOrdersService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.siot.IamportRestClient.exception.IamportResponseException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
public class AdminOrdersController {

    private final AdminOrdersService adminOrdersService;

    @PostMapping("/refund/all")
    public BaseResponse<Boolean> refundAll(
        @RequestBody AdminOrderDTO.AllRefund dto,
        @AuthenticationPrincipal PrincipalDetails user)
        throws IamportResponseException, IOException {
        log.info("refundAll API 호출 - {}, dto- {}", user.getUser().getName(), dto);
        return new BaseResponse<Boolean>(
            adminOrdersService.refundAll(dto, user));
    }

    @PostMapping("/refund/partial")
    public BaseResponse<Boolean> refundPartially(
        @RequestBody AdminOrderDTO.PartialRefund dto,
        @AuthenticationPrincipal PrincipalDetails user)
        throws IamportResponseException, IOException {
        log.info("refundPartially API 호출 - {}, dto- {}", user.getUser().getName(), dto);
        return new BaseResponse<Boolean>(
            adminOrdersService.refundPartially(dto, user));
    }
}
