package com.metalancer.backend.category.controller;


import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.category.dto.CategoryDTO.TrendSpotlightCategory;
import com.metalancer.backend.category.service.CategoryListService;
import com.metalancer.backend.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "카테고리 목록", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/category/list")
public class CategoryListController {

    private final CategoryListService categoryListService;

    @Operation(summary = "메인페이지-Hot Pick 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = MainCategory.class)))
    })
    @GetMapping("/hot-pick")
    public BaseResponse<List<MainCategory>> getHotPickCategoryList() {
        return new BaseResponse<>(
            categoryListService.getHotPickCategoryList());
    }

    @Operation(summary = "메인페이지-Trend Spotlight 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = TrendSpotlightCategory.class)))
    })
    @GetMapping("/trend-spotlight")
    public BaseResponse<List<TrendSpotlightCategory>> getTrendSpotlightCategoryList() {
        return new BaseResponse<>(
            categoryListService.getTrendSpotlightCategoryList());
    }

    @Operation(summary = "메인페이지-Genre Galaxy 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = MainCategory.class)))
    })
    @GetMapping("/genre-galaxy")
    public BaseResponse<List<MainCategory>> getGenreGalaxyCategoryList() {
        return new BaseResponse<>(
            categoryListService.getGenreGalaxyCategoryList());
    }

    @Operation(summary = "제작 요청-인기 분류", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = RequestCategory.class)))
    })
    @GetMapping("/request")
    public BaseResponse<List<RequestCategory>> getRequestCategoryList() {
        return new BaseResponse<>(
            categoryListService.getRequestCategoryList());
    }

    @Operation(summary = "에셋 등록 - 태그 등록 시 추천검색어", description = "최대 10개까지만 조회됩니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/tag-register")
    public BaseResponse<List<String>> getTagRegisterRecommendList(
        @RequestParam("keyword") String keyword) {
        return new BaseResponse<List<String>>(
            categoryListService.getTagRegisterRecommendList(keyword));
    }
}
