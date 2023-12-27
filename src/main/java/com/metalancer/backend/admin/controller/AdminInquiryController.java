package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.InquiryList;
import com.metalancer.backend.admin.dto.AdminMemberDTO;
import com.metalancer.backend.admin.service.AdminInquiryService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/inquiry")
public class AdminInquiryController {

    private final AdminInquiryService adminInquiryService;

    @GetMapping("/list")
    public BaseResponse<Page<InquiryList>> getAdminInquiryList(
        @Parameter(description = "페이징") Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageableDescending(pageable);
        return new BaseResponse<Page<InquiryList>>(
            adminInquiryService.getAdminInquiryList(pageable));
    }

    @GetMapping("/new/count")
    public BaseResponse<Integer> getAdminInquiryNewCount() {
        return new BaseResponse<Integer>(
            adminInquiryService.getAdminInquiryNewCount());
    }

    @PatchMapping("/{inquiryId}")
    public BaseResponse<Boolean> replyInquiry(
        @AuthenticationPrincipal PrincipalDetails user,
        @PathVariable("inquiryId") Long inquiryId,
        @RequestBody AdminMemberDTO.CreateUpdateReply dto) {
        return new BaseResponse<Boolean>(
            adminInquiryService.replyInquiry(user, inquiryId, dto));
    }


}
