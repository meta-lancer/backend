package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.ProductsTypeAndTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsTypeAndTagJpaRepository extends JpaRepository<ProductsTypeAndTagEntity, Long> {

}
