package com.metalancer.backend.request.repository;

import com.metalancer.backend.request.entity.ProductsRequestCommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRequestCommentsJpaRepository extends
    JpaRepository<ProductsRequestCommentsEntity, Long> {


}
