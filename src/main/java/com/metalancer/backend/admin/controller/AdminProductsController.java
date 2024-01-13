package com.metalancer.backend.admin.controller;


import com.metalancer.backend.admin.domain.ProductsList;
import com.metalancer.backend.admin.service.AdminProductsService;
import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.response.BaseResponse;
import com.metalancer.backend.common.utils.PageFunction;
import com.metalancer.backend.products.domain.ProductsDetail;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductsController {

    private final AdminProductsService adminProductsService;

    @GetMapping("/list")
    public BaseResponse<Page<ProductsList>> getAdminProductsList(
        @AuthenticationPrincipal PrincipalDetails user,
        @Parameter(description = "페이징") Pageable pageable) throws Exception {
        pageable = PageFunction.convertToOneBasedPageableDescending(pageable);
        return new BaseResponse<Page<ProductsList>>(
            adminProductsService.getAdminProductsList(pageable));
    }

    @GetMapping("/{productId}")
    public BaseResponse<ProductsDetail> getAdminProductDetail(
        @AuthenticationPrincipal PrincipalDetails user,
        @PathVariable("productId") Long productId
    ) {
        return new BaseResponse<ProductsDetail>(adminProductsService.getProductDetail(productId));
    }
}
