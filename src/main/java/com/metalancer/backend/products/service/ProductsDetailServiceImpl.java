package com.metalancer.backend.products.service;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.domain.ProductsDetail;
import com.metalancer.backend.products.entity.Products;
import com.metalancer.backend.products.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsDetailServiceImpl implements ProductsDetailService {

    private final ProductsRepository productsRepository;

    @Override
    public String getProductDetailSharedLink(Long productId) {
        // 상품 조회
        // return 도메인 url + 상품의 공유 고유번호
        return "";
    }

    @Override
    public boolean toggleProductWish(PrincipalDetails user, Long productId) {
        // user와 상품 고유번호를 가지고 찜하기 조회
        // 데이터가 없다면 추가, 있다면 삭제
        // return 추가 - true, 삭제 - false
        return false;
    }

    @Override
    public ProductsDetail getProductDetail(Long productId) {
        Products foundProducts = productsRepository.findProductByIdAndStatus(productId,
            DataStatus.ACTIVE);
        return foundProducts.toProductsDetail();
    }
}