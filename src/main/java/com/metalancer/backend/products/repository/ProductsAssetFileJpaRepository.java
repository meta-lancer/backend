package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductsAssetFileJpaRepository extends
    JpaRepository<ProductsAssetFileEntity, Long> {

    Optional<ProductsAssetFileEntity> findByProductsEntity(ProductsEntity productsEntity);

    @Query("select paf from products_asset_file paf where paf.productsEntity.creatorEntity = :creator and paf.success = true order by paf.createdAt desc ")
    Page<ProductsAssetFileEntity> findAllByCreator(@Param("creator") CreatorEntity creatorEntity,
        Pageable pageable);
}
