package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRequestOptionJpaRepository extends
    JpaRepository<ProductsRequestOptionEntity, Long> {

    List<ProductsRequestOptionEntity> findAllByProductsEntityAndStatus(
        ProductsEntity foundProductsEntity, DataStatus status);

    Optional<ProductsRequestOptionEntity> findByProductsEntityAndIdAndStatus(
        ProductsEntity foundProductsEntity, Long id, DataStatus status);
}
