package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsThumbnailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsThumbnailJpaRepository extends
    JpaRepository<ProductsThumbnailEntity, Long> {


}
