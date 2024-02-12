package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.TrendSpotlightCategory;
import com.metalancer.backend.admin.service.AdminCategoryService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.CategoryType;
import com.metalancer.backend.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @Operation(summary = "어드민-Hot Pick 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/hot-pick")
    public BaseResponse<List<CategoryList>> getAdminHotPickCategoryList(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<>(
            adminCategoryService.getAdminHotPickCategoryList());
    }

    @Operation(summary = "어드민-Trend Spotlight 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/trend-spotlight")
    public BaseResponse<List<TrendSpotlightCategory>> getAdminTrendSpotlightCategoryList(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<>(
            adminCategoryService.getAdminTrendSpotlightCategoryList());
    }

    @Operation(summary = "메인페이지-Genre Galaxy 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/genre-galaxy")
    public BaseResponse<List<CategoryList>> getAdminGenreGalaxyCategoryList(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<>(
            adminCategoryService.getAdminGenreGalaxyCategoryList());
    }

    @Operation(summary = "어드민 - 제작 요청-인기 분류", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/request")
    public BaseResponse<List<CategoryList>> getAdminRequestCategoryList(
        @AuthenticationPrincipal PrincipalDetails user) {
        return new BaseResponse<>(
            adminCategoryService.getAdminRequestCategoryList());
    }

    @Operation(summary = "어드민 hot pick 카테고리 사용 변경", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/hot-pick/{categoryId}")
    public BaseResponse<Boolean> updateHotPickCategoryUseYn(
        @PathVariable("categoryId") Long categoryId,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("어드민 hot pick 카테고리 사용 변경 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<>(
            adminCategoryService.updateCategoryUseYn(CategoryType.HOT_PICK, categoryId));
    }

    @Operation(summary = "어드민 trend-spotlight 카테고리 사용 변경", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/trend-spotlight/{categoryId}")
    public BaseResponse<Boolean> updateTrendSpotlightCategoryUseYn(
        @PathVariable("categoryId") Long categoryId,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("어드민 trend-spotlight 카테고리 사용 변경 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<>(
            adminCategoryService.updateCategoryUseYn(CategoryType.TREND_SPOTLIGHT, categoryId));
    }

    @Operation(summary = "어드민 genre-galaxy 카테고리 사용 변경", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/genre-galaxy/{categoryId}")
    public BaseResponse<Boolean> updateGenreGalaxyCategoryUseYn(
        @PathVariable("categoryId") Long categoryId,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("어드민 genre-galaxy 카테고리 사용 변경 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<>(
            adminCategoryService.updateCategoryUseYn(CategoryType.GENRE_GALAXY, categoryId));
    }

    @Operation(summary = "어드민 request 카테고리 사용 변경", description = "")
    @ApiResponse(responseCode = "200", description = "처리 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PatchMapping("/request/{categoryId}")
    public BaseResponse<Boolean> updateRequestCategoryUseYn(
        @PathVariable("categoryId") Long categoryId,
        @AuthenticationPrincipal PrincipalDetails user) {
        log.info("어드민 request 카테고리 사용 변경 API 호출 - 고유번호 {}, 이름 {}", user.getUser().getId(),
            user.getUser().getName());
        return new BaseResponse<>(
            adminCategoryService.updateCategoryUseYn(CategoryType.REQUEST, categoryId));
    }
}
