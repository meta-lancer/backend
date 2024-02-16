package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.UserCompletedOrder;
import com.metalancer.backend.admin.dto.AdminOrderDTO;
import com.metalancer.backend.admin.service.AdminOrdersService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.AuthUtils;
import com.metalancer.backend.common.utils.PageFunction;
import com.siot.IamportRestClient.exception.IamportResponseException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public BaseResponse<Page<UserCompletedOrder>> getOrderList(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        log.info("getOrderList API 호출 - {}", user.getUser().getName());
        pageable = PageFunction.convertToOneBasedPageableDescending(pageable);
        return new BaseResponse<Page<UserCompletedOrder>>(
            adminOrdersService.getOrderList(pageable));
    }

    @PostMapping("/refund/all")
    public BaseResponse<Boolean> refundAll(
        @RequestBody AdminOrderDTO.AllRefund dto,
        @AuthenticationPrincipal PrincipalDetails user)
        throws IamportResponseException, IOException {
        AuthUtils.validateUserAuthentication(user);
        log.info("refundAll API 호출 - {}, dto- {}", user.getUser().getName(), dto);
        return new BaseResponse<Boolean>(
            adminOrdersService.refundAll(dto, user));
    }

    @PostMapping("/refund/partial")
    public BaseResponse<Boolean> refundPartially(
        @RequestBody AdminOrderDTO.PartialRefund dto,
        @AuthenticationPrincipal PrincipalDetails user)
        throws IamportResponseException, IOException {
        AuthUtils.validateUserAuthentication(user);
        log.info("refundPartially API 호출 - {}, dto- {}", user.getUser().getName(), dto);
        return new BaseResponse<Boolean>(
            adminOrdersService.refundPartially(dto, user));
    }
}
