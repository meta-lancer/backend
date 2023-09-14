package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsViewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsViewsJpaRepository extends
    JpaRepository<ProductsViewsEntity, Long> {


}
