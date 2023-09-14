package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsAssetFileRepositoryImpl implements ProductsAssetFileRepository {

    private final ProductsAssetFileJpaRepository productsAssetFileJpaRepository;

    @Override
    public void save(ProductsAssetFileEntity createdProductsAssetFileEntity) {
        productsAssetFileJpaRepository.save(createdProductsAssetFileEntity);
    }

    @Override
    public String findUrlByProduct(ProductsEntity productsEntity) {
        ProductsAssetFileEntity foundProductsEntity = productsAssetFileJpaRepository.findByProductsEntity(
            productsEntity).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
        ;
        return foundProductsEntity.getUrl();
    }
}
