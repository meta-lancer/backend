package com.metalancer.backend.request.repository;

import com.metalancer.backend.request.entity.ProductsRequestAndTypeEntity;
import com.metalancer.backend.request.entity.ProductsRequestEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRequestAndTypeJpaRepository extends
    JpaRepository<ProductsRequestAndTypeEntity, Long> {

    List<ProductsRequestAndTypeEntity> findAllByProductsRequestEntity(
        ProductsRequestEntity productsRequestEntity);
}
