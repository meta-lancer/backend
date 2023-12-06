package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.creators.domain.CreatorAssetList;
import com.metalancer.backend.products.entity.ProductsAssetFileEntity;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsAssetFileRepositoryImpl implements ProductsAssetFileRepository {

    private final ProductsAssetFileJpaRepository productsAssetFileJpaRepository;

    @Override
    public void save(ProductsAssetFileEntity createdProductsAssetFileEntity) {
        productsAssetFileJpaRepository.save(createdProductsAssetFileEntity);
    }

    @Override
    public String findUrlByProduct(ProductsEntity productsEntity) {
        ProductsAssetFileEntity foundProductsEntity = productsAssetFileJpaRepository.findByProductsEntity(
            productsEntity).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
        ;
        return foundProductsEntity.getUrl();
    }

    @Override
    public ProductsAssetFileEntity findByProducts(ProductsEntity productsEntity) {
        return productsAssetFileJpaRepository.findByProductsEntity(
            productsEntity).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public Optional<ProductsAssetFileEntity> findOptionalEntityByProducts(
        ProductsEntity productsEntity) {
        return productsAssetFileJpaRepository.findByProductsEntity(
            productsEntity);
    }

    @Override
    public Page<CreatorAssetList> findAllCreatorAssetListByCreator(CreatorEntity creatorEntity,
        Pageable pageable) {
        Page<ProductsAssetFileEntity> productsAssetFileEntities = productsAssetFileJpaRepository.findAllByCreator(
            creatorEntity, pageable);
        return productsAssetFileEntities.map(ProductsAssetFileEntity::toCreatorAssetList);
    }

    @Override
    public Page<CreatorAssetList> findAllMyAssetList(CreatorEntity creatorEntity,
        Pageable pageable) {
        Page<ProductsAssetFileEntity> productsAssetFileEntities = productsAssetFileJpaRepository.findAllByCreator(
            creatorEntity, pageable);
        return productsAssetFileEntities.map(ProductsAssetFileEntity::toCreatorAssetList);
    }
}

