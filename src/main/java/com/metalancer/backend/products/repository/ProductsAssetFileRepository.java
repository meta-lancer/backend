package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import java.util.Optional;

public interface ProductsAssetFileRepository {

    void save(ProductsAssetFileEntity createdProductsAssetFileEntity);

    String findUrlByProduct(ProductsEntity savedProductsEntity);

    ProductsAssetFileEntity findByProducts(ProductsEntity productsEntity);

    Optional<ProductsAssetFileEntity> findOptionalEntityByProducts(ProductsEntity productsEntity);
}
