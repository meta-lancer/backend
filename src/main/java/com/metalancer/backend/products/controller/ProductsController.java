package com.metalancer.backend.products.controller;


import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.products.service.ProductsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductsService productsService;

    @Operation(summary = "상품 검색", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/search")
    public BaseResponse<String> searchProducts(@Parameter(description = "검색어") String keyword) {
        log.info("검색어: {}", keyword);
        return new BaseResponse<>(productsService.searchProducts(keyword));
    }
}
