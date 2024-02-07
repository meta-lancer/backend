package com.metalancer.backend.request.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.request.entity.ProductsRequestCommentsEntity;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRequestCommentsJpaRepository extends
    JpaRepository<ProductsRequestCommentsEntity, Long> {

    Page<ProductsRequestCommentsEntity> findAllByProductsRequestEntityAndStatus(
        ProductsRequestEntity productsRequestEntity, DataStatus status, Pageable pageable);

    Optional<ProductsRequestCommentsEntity> findByProductsRequestEntityAndId(
        ProductsRequestEntity productsRequestEntity, Long id);
}
