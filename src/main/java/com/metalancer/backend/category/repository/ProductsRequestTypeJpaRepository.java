package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRequestTypeJpaRepository extends
    JpaRepository<ProductsRequestTypeEntity, Long> {

}
