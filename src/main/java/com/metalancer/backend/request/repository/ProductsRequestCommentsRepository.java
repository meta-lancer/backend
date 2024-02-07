package com.metalancer.backend.request.repository;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.request.entity.ProductsRequestCommentsEntity;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsRequestCommentsRepository {


    Page<ProductsRequestCommentsEntity> findAllByPage(ProductsRequestEntity productsRequestEntity,
        PrincipalDetails user, Pageable pageable);

    void save(ProductsRequestCommentsEntity createdProductsRequestCommentsEntity);

    Optional<ProductsRequestCommentsEntity> findOptionalById(Long id);

    ProductsRequestCommentsEntity findByProductsRequestEntityAndId(
        ProductsRequestEntity productsRequestEntity, Long commentId);
}
