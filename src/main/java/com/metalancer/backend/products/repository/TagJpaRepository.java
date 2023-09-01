package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.TagEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagJpaRepository extends JpaRepository<TagEntity, Long> {

    List<TagEntity> findAllByProductsEntityOrderByNameAsc(ProductsEntity products);
}
