package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsViewsEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsViewsJpaRepository extends
    JpaRepository<ProductsViewsEntity, Long> {

    List<ProductsViewsEntity> findAllByProductsEntityOrderByViewOrdAsc(
        ProductsEntity productsEntity);
}
