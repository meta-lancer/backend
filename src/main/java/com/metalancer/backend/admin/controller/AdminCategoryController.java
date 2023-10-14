package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.service.AdminCategoryService;
import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/category")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @Operation(summary = "메인페이지-Hot Pick 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/hot-pick")
    public BaseResponse<List<MainCategory>> getAdminHotPickCategoryList() {
        return new BaseResponse<>(
            adminCategoryService.getAdminHotPickCategoryList());
    }

    @Operation(summary = "메인페이지-Trend Spotlight 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/trend-spotlight")
    public BaseResponse<List<MainCategory>> getAdminTrendSpotlightCategoryList() {
        return new BaseResponse<>(
            adminCategoryService.getAdminTrendSpotlightCategoryList());
    }

    @Operation(summary = "메인페이지-Genre Galaxy 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/genre-galaxy")
    public BaseResponse<List<MainCategory>> getAdminGenreGalaxyCategoryList() {
        return new BaseResponse<>(
            adminCategoryService.getAdminGenreGalaxyCategoryList());
    }

    @Operation(summary = "제작 요청-인기 분류", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/request")
    public BaseResponse<List<RequestCategory>> getAdminRequestCategoryList() {
        return new BaseResponse<>(
            adminCategoryService.getAdminRequestCategoryList());
    }
}
