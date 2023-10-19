package com.metalancer.backend.products.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.service.ProductsDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품 상세", description = "")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductsDetailController {

    private final ProductsDetailService productsDetailService;

    @Operation(summary = "상품 상세 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/{productId}")
    public BaseResponse<ProductsDetail> getProductDetail(
        @AuthenticationPrincipal
        PrincipalDetails user,
        @PathVariable("productId") Long productId
    ) {
        log.info("상품 고유번호: {}", productId);
        return new BaseResponse<>(productsDetailService.getProductDetail(user, productId));
    }

    @Operation(summary = "상품 링크 공유", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/{productId}/shared-link")
    public BaseResponse<String> getProductDetailSharedLink(
        @PathVariable("productId") Long productId) {
        log.info("링크 공유 상품고유번호: {}", productId);
        return new BaseResponse<>(productsDetailService.getProductDetailSharedLink(productId));
    }

    @Operation(summary = "상품 찜하기", description = "추가하면 true, 삭제/실패하면 false")
    @ApiResponse(responseCode = "200", description = "토글 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @PostMapping("/{productId}/wish")
    public BaseResponse<Boolean> toggleProductWish(@AuthenticationPrincipal
    PrincipalDetails user, @PathVariable("productId") Long productId) {
        log.info("찜하기 상품고유번호: {}", productId);
        return new BaseResponse<>(productsDetailService.toggleProductWish(user, productId));
    }

    @Operation(summary = "링크 공유를 통한 상품 상세 조회", description = "")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @GetMapping("/shared-link")
    public BaseResponse<String> getProductDetailBySharedLink(
        @RequestParam("link") String link) {
        return new BaseResponse<String>(productsDetailService.getProductDetailBySharedLink(link));
    }
}
