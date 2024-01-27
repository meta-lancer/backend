package com.metalancer.backend.request.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestCommentsRepositoryImpl implements ProductsRequestCommentsRepository {

    private final ProductsRequestCommentsJpaRepository productsRequestCommentsJpaRepository;
}
