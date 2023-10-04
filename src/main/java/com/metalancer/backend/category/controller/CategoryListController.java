package com.metalancer.backend.category.controller;


import com.metalancer.backend.category.service.CategoryListService;
import com.metalancer.backend.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "카테고리 목록", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/category/list")
public class CategoryListController {

    private final CategoryListService categoryListService;

    @Operation(summary = "메인페이지-Hot Pick 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/hot-pick")
    public BaseResponse<List<String>> getHotPickCategoryList() {
        return new BaseResponse<>(
            categoryListService.getHotPickCategoryList());
    }

    @Operation(summary = "메인페이지-Trend Spotlight 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/trend-spotlight")
    public BaseResponse<List<String>> getTrendSpotlightCategoryList() {
        return new BaseResponse<>(
            categoryListService.getTrendSpotlightCategoryList());
    }

    @Operation(summary = "메인페이지-Genre Galaxy 카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/genre-galaxy")
    public BaseResponse<List<String>> getGenreGalaxyCategoryList() {
        return new BaseResponse<>(
            categoryListService.getGenreGalaxyCategoryList());
    }

}
