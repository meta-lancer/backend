package com.metalancer.backend.products.controller;


import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.products.service.ProductsDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductsDetailController {

    private final ProductsDetailService productsDetailService;

    @GetMapping("/{productId}/shared-link")
    public BaseResponse<String> getProductDetailSharedLink(
        @PathVariable("productId") Long productId) {
        log.info("링크 공유 상품고유번호: {}", productId);
        return new BaseResponse<>(productsDetailService.getProductDetailSharedLink(productId));
    }

    @PostMapping("/{productId}/wish")
    public BaseResponse<Boolean> toggleProductWish(@AuthenticationPrincipal
    PrincipalDetails user, @PathVariable("productId") Long productId) {
        log.info("찜하기 상품고유번호: {}", productId);
        return new BaseResponse<>(productsDetailService.toggleProductWish(user, productId));
    }
}
