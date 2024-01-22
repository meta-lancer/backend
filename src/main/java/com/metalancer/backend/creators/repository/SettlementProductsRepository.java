package com.metalancer.backend.creators.repository;

import com.metalancer.backend.common.constants.SettlementStatus;
import com.metalancer.backend.creators.entity.SettlementProductsEntity;
import com.metalancer.backend.products.entity.ProductsEntity;

public interface SettlementProductsRepository {

    int countAllByProducts(ProductsEntity productsEntity, SettlementStatus settlementStatus);

    void save(SettlementProductsEntity settlementProductsEntity);
}
