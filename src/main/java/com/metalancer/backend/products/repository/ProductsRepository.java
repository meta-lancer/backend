package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsRepository {

    ProductsEntity findProductById(Long productsId);

    ProductsEntity findProductByIdAndStatus(Long productsId, DataStatus status);

    Page<ProductsEntity> findProductsListByCreator(CreatorEntity creatorEntity, Pageable pageable);

    Page<ProductsEntity> findProductsListByCreatorAndStatus(CreatorEntity creatorEntity,
        DataStatus status,
        Pageable pageable);

    Page<ProductsEntity> findAllByCreator(CreatorEntity creatorEntity, Pageable pageable);

    Page<ProductsEntity> findAllByCreatorAndStatus(CreatorEntity creatorEntity, DataStatus status,
        Pageable pageable);

    Page<HotPickAsset> findNewProductList(Pageable pageable);

    Page<HotPickAsset> findSaleProductList(Pageable pageable);

    Page<HotPickAsset> findFreeProductList(PeriodType period, Pageable pageable);

    Page<HotPickAsset> findChargeProductList(PeriodType period, Pageable pageable);

    void save(ProductsEntity createdProductsEntity);

    long countAllByCreatorEntity(CreatorEntity creatorEntity);
}
