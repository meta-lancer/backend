package com.metalancer.backend.products.controller;


import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.ProperAssetType;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.products.service.ProductsListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 목록", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products/list")
public class ProductsListController {

    private ProductsListService productsListService;

    @Operation(summary = "에셋 Hot Pick", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/hot-pick")
    public BaseResponse<String> getHotPickList(
        @Parameter(description = "종류") @RequestParam HotPickType type,
        @Parameter(description = "페이징") @RequestParam Pageable pageable) {
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(productsListService.getHotPickList(type, adjustedPageable));
    }

    @Operation(summary = "당신에게 어울리는 에셋", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/proper-asset")
    public BaseResponse<String> getProperAssetList(
        @Parameter(description = "종류") @RequestParam ProperAssetType type,
        @Parameter(description = "페이징") @RequestParam Pageable pageable) {
        Pageable adjustedPageable = PageFunction.convertToOneBasedPageable(pageable);
        return new BaseResponse<>(productsListService.getProperAssetList(type, adjustedPageable));
    }
}
