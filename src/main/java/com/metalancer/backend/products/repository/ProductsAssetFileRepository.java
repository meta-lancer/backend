package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;

public interface ProductsAssetFileRepository {

    void save(ProductsAssetFileEntity createdProductsAssetFileEntity);

    String findUrlByProduct(ProductsEntity savedProductsEntity);
}
