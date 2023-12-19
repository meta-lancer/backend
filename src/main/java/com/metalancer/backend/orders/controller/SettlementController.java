package com.metalancer.backend.orders.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.orders.domain.DaySalesReport;
import com.metalancer.backend.orders.domain.SettlementRecordList;
import com.metalancer.backend.orders.domain.SettlementReportList;
import com.metalancer.backend.orders.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "정산관리", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/settlement")
public class SettlementController {

    private final SalesService salesService;

    @Operation(summary = "대시보드", description = "주간, 월간 제공")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = DaySalesReport.class)))
    @GetMapping("/sales/day")
    public BaseResponse<List<DaySalesReport>> getDaySalesReport(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestParam("periodType") PeriodType periodType) {
        return new BaseResponse<>(salesService.getDaySalesReport(user, periodType));
    }

    @Operation(summary = "대시보드-엑셀출력", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = DaySalesReport.class)))
    @GetMapping("/sales/day/excel")
    public BaseResponse<List<DaySalesReport>> getDaySalesReportByExcel(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestParam("beginDate") String beginDate,
        @RequestParam("endDate") String endDate) {
        return new BaseResponse<>(salesService.getDaySalesReportByExcel(user, beginDate, endDate));
    }

    @Operation(summary = "정산관리-정산리포트", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = SettlementReportList.class)))
    @GetMapping
    public BaseResponse<Page<SettlementReportList>> getSettlementReportList(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(salesService.getSettlementReportList(user, pageable));
    }

    @Operation(summary = "정산관리-정산 기록", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = SettlementReportList.class)))
    @GetMapping("/record")
    public BaseResponse<Page<SettlementRecordList>> getSettlementRecordList(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(salesService.getSettlementRecordList(user, pageable));
    }
}
