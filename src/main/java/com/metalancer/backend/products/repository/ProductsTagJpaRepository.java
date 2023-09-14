package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsTagEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsTagJpaRepository extends JpaRepository<ProductsTagEntity, Long> {

    List<ProductsTagEntity> findAllByProductsEntityOrderByNameAsc(ProductsEntity products);
}
