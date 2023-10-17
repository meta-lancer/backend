package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsAssetFileJpaRepository extends
    JpaRepository<ProductsAssetFileEntity, Long> {

    Optional<ProductsAssetFileEntity> findByProductsEntity(ProductsEntity productsEntity);
}
