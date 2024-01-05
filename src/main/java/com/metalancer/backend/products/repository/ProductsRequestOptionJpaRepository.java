package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRequestOptionJpaRepository extends
    JpaRepository<ProductsRequestOptionEntity, Long> {

    List<ProductsRequestOptionEntity> findAllByProductsEntityAndStatus(
        ProductsEntity foundProductsEntity, DataStatus status);
}
