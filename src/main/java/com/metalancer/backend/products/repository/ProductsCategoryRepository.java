package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsCategoryRepository extends JpaRepository<ProductsCategoryEntity, Long> {

}
