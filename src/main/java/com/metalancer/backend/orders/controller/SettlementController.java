package com.metalancer.backend.orders.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.AuthUtils;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.orders.domain.DaySalesReport;
import com.metalancer.backend.orders.domain.EachSalesReport;
import com.metalancer.backend.orders.domain.SettlementRecordList;
import com.metalancer.backend.orders.domain.SettlementReportList;
import com.metalancer.backend.orders.domain.SettlementRequestInfo;
import com.metalancer.backend.orders.domain.SettlementRequestList;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<>(salesService.getDaySalesReport(user, periodType));
    }

    @Operation(summary = "대시보드-엑셀출력", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = DaySalesReport.class)))
    @GetMapping("/sales/day/excel")
    public BaseResponse<List<DaySalesReport>> getDaySalesReportByExcel(
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestParam("beginDate") String beginDate,
        @RequestParam("endDate") String endDate) {
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<>(salesService.getDaySalesReportByExcel(user, beginDate, endDate));
    }

    @Operation(summary = "정산관리-정산요청 가능여부", description = "true면 정산요청 가능. 정산요청 불가능의 경우: 1. 판매 건수 0. 2. 이미 정산 전체 완료 3. 이미 정산요청 진행함(최근 정산 요청일이 판매내역보다 더 뒤인 경우)")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = SettlementReportList.class)))
    @GetMapping("/request/possiblity")
    public BaseResponse<Boolean> checkSettlementRequestAvailable(
        @AuthenticationPrincipal PrincipalDetails user) {
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<Boolean>(salesService.checkSettlementRequestAvailable(user));
    }

    @Operation(summary = "정산관리-정산리포트", description = "정산상태: 판매한 갯수와 정산 요청된 갯수가 같다면 true, 아니면 false(판매갯수 0이라면도 추가!")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = SettlementReportList.class)))
    @GetMapping
    public BaseResponse<Page<SettlementReportList>> getSettlementReportList(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        AuthUtils.validateUserAuthentication(user);
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(salesService.getSettlementReportList(user, pageable));
    }

    @Operation(summary = "정산관리-정산 기록", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = SettlementReportList.class)))
    @GetMapping("/record")
    public BaseResponse<Page<SettlementRecordList>> getSettlementRecordList(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        AuthUtils.validateUserAuthentication(user);
        pageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(salesService.getSettlementRecordList(user, pageable));
    }

    @Operation(summary = "정산관리-정산리포트 상품별", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = EachSalesReport.class)))
    @GetMapping("/products/{productsId}")
    public BaseResponse<EachSalesReport> getSettlementProductsReport(
        @PathVariable("productsId") Long productsId,
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestParam("periodType") PeriodType periodType) {
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<EachSalesReport>(
            salesService.getSettlementProductsReport(productsId, user, periodType));
    }

    @Operation(summary = "정산관리-정산리포트 상품별 엑셀출력", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = DaySalesReport.class)))
    @GetMapping("/products/{productsId}/excel")
    public BaseResponse<List<DaySalesReport>> getSettlementProductsReportByExcel(
        @PathVariable("productsId") Long productsId,
        @AuthenticationPrincipal PrincipalDetails user,
        @RequestParam("beginDate") String beginDate,
        @RequestParam("endDate") String endDate) {
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<>(
            salesService.getProductsDaySalesReportByExcel(productsId, user, beginDate, endDate));
    }

    @Operation(summary = "정산관리-정산요청 시 상세내용 상품리스트", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = SettlementRequestList.class)))
    @GetMapping("/request/products")
    public BaseResponse<Page<SettlementRequestList>> getSettlementRequestList(
        @AuthenticationPrincipal PrincipalDetails user,
        Pageable pageable) {
        AuthUtils.validateUserAuthentication(user);
        pageable = PageFunction.convertToOneBasedPageableDescending(pageable);
        return new BaseResponse<>(
            salesService.getSettlementRequestList(user, pageable));
    }

    @Operation(summary = "정산관리-정산요청 시 상세 정보", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = SettlementRequestList.class)))
    @GetMapping("/request/info")
    public BaseResponse<SettlementRequestInfo> getSettlementRequestInfo(
        @AuthenticationPrincipal PrincipalDetails user) {
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<SettlementRequestInfo>(
            salesService.getSettlementRequestInfo(user));
    }

    // 정산요청
    @Operation(summary = "정산관리-정산요청", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    @PostMapping("/request")
    public BaseResponse<Boolean> createSettlementRequest(
        @AuthenticationPrincipal PrincipalDetails user) {
        AuthUtils.validateUserAuthentication(user);
        return new BaseResponse<Boolean>(
            salesService.createSettlementRequest(user));
    }
}
