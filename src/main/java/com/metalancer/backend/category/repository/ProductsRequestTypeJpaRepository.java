package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.ProductsRequestTypeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRequestTypeJpaRepository extends
    JpaRepository<ProductsRequestTypeEntity, Long> {

    Optional<ProductsRequestTypeEntity> findByName(String name);

    List<ProductsRequestTypeEntity> findAllByUseYnTrue();
}
