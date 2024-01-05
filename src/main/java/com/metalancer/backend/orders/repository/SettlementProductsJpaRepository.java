package com.metalancer.backend.orders.repository;

import com.metalancer.backend.orders.entity.SettlementProductsEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementProductsJpaRepository extends
    JpaRepository<SettlementProductsEntity, Long> {

    List<SettlementProductsEntity> findAllByProductsEntity(ProductsEntity productsEntity);
}
