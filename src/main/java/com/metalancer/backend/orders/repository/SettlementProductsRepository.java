package com.metalancer.backend.orders.repository;

import com.metalancer.backend.products.entity.ProductsEntity;

public interface SettlementProductsRepository {

    int countAllByProducts(ProductsEntity productsEntity);
}
