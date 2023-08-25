package com.metalancer.backend.products.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface ProductsDetailService {

    String getProductDetailSharedLink(Long productId);

    boolean toggleProductWish(@AuthenticationPrincipal
    PrincipalDetails user, Long productId);
}