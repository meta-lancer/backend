package com.metalancer.backend.creators.repository;

import com.metalancer.backend.products.entity.ProductsEntity;

public interface SettlementProductsRepository {

    int countAllByProducts(ProductsEntity productsEntity);
}
