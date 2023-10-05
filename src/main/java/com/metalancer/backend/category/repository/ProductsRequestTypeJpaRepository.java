package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsRequestTypeJpaRepository extends
        JpaRepository<ProductsRequestTypeEntity, Long> {

    Optional<ProductsRequestTypeEntity> findByName(String name);
}
