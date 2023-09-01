package com.metalancer.backend.products.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.products.domain.ProductsDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface ProductsDetailService {

    String getProductDetailSharedLink(Long productId);

    boolean toggleProductWish(@AuthenticationPrincipal
    PrincipalDetails user, Long productId);

    ProductsDetail getProductDetail(Long productId);
}