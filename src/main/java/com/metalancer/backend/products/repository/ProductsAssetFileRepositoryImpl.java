package com.metalancer.backend.products.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsAssetFileRepositoryImpl implements ProductsAssetFileRepository {

    private final ProductsAssetFileJpaRepository productsAssetFileJpaRepository;

}
