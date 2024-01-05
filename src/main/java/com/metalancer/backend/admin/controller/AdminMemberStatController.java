package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.MemberRegisterDailyStat;
import com.metalancer.backend.admin.domain.MemberRegisterStat;
import com.metalancer.backend.admin.service.AdminMemberStatService;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/member/stat")
public class AdminMemberStatController {

    private final AdminMemberStatService adminMemberStatService;

    @GetMapping
    public BaseResponse<MemberRegisterStat> getAdminMemberStat() {
        return new BaseResponse<MemberRegisterStat>(adminMemberStatService.getAdminMemberStat());
    }

    @GetMapping("/daily")
    public BaseResponse<List<MemberRegisterDailyStat>> getAdminMemberRegisterDailyStat(
        @RequestParam(value = "beginDate", required = false) String beginDate,
        @RequestParam(value = "endDate", required = false) String endDate,
        @RequestParam("periodType") PeriodType periodType) {
        return new BaseResponse<List<MemberRegisterDailyStat>>(
            adminMemberStatService.getAdminMemberRegisterDailyStat(beginDate, endDate, periodType));
    }
}
