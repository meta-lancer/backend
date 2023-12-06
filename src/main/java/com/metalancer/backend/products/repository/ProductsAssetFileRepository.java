package com.metalancer.backend.products.repository;

import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsAssetFileRepository {

    void save(ProductsAssetFileEntity createdProductsAssetFileEntity);

    String findUrlByProduct(ProductsEntity savedProductsEntity);

    ProductsAssetFileEntity findByProducts(ProductsEntity productsEntity);

    Optional<ProductsAssetFileEntity> findOptionalEntityByProducts(ProductsEntity productsEntity);

    Page<CreatorAssetList> findAllCreatorAssetListByCreator(CreatorEntity creatorEntity,
        Pageable pageable);

    Page<CreatorAssetList> findAllMyAssetList(CreatorEntity creatorEntity,
        Pageable pageable);
}
