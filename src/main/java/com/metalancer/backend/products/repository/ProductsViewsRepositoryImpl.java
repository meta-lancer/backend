package com.metalancer.backend.products.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsViewsRepositoryImpl implements ProductsViewsRepository {

    private final ProductsViewsJpaRepository productsViewsJpaRepository;

}
