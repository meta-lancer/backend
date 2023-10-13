package com.metalancer.backend.request.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestAndTypeRepositoryImpl implements ProductsRequestAndTypeRepository {

    private final ProductsRequestAndTypeJpaRepository productsRequestAndTypeJpaRepository;

}
