package com.metalancer.backend.category.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsTypeAndTagRepositoryImpl implements ProductsTypeAndTagRepository {

    private final ProductsTypeAndTagJpaRepository productsTypeAndTagJpaRepository;

}
