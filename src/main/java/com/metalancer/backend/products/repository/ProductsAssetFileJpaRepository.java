package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsAssetFileJpaRepository extends
    JpaRepository<ProductsAssetFileEntity, Long> {


}
