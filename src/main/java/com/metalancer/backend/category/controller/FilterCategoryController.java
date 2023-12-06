package com.metalancer.backend.category.controller;


import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "필터 카테고리 목록", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/category/filter")
public class FilterCategoryController {

    private final CategoryListService categoryListService;

    @Operation(summary = "필터 에셋 목록-카테고리", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = MainCategory.class)))
    })
    @GetMapping("/category")
    public BaseResponse<List<MainCategory>> getFilterCategoryList() {
        return new BaseResponse<List<MainCategory>>(
            categoryListService.getFilterCategoryList());
    }

    @Operation(summary = "필터 에셋 목록-플랫폼", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = {
        @Content(array = @ArraySchema(schema = @Schema(implementation = MainCategory.class)))
    })
    @GetMapping("/platform")
    public BaseResponse<List<MainCategory>> getFilterPlatformList() {
        return new BaseResponse<List<MainCategory>>(
            categoryListService.getFilterPlatformList());
    }
}
