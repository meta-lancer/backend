package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsThumbnailEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsThumbnailJpaRepository extends
    JpaRepository<ProductsThumbnailEntity, Long> {

    List<ProductsThumbnailEntity> findAllByProductsEntityOrderByThumbnailOrdAsc(
        ProductsEntity productsEntity);

}
