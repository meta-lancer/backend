package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsRepository {

    ProductsEntity findProductById(Long productsId);

    ProductsEntity findProductBySharedLinkAndStatus(String SharedLink, DataStatus status);

    ProductsEntity findProductByIdAndStatus(Long productsId, DataStatus status);

    ProductsEntity findAdminProductById(Long productsId);


    Page<ProductsEntity> findProductsListByCreator(CreatorEntity creatorEntity, Pageable pageable);

    Page<ProductsEntity> findProductsListByCreatorAndStatus(CreatorEntity creatorEntity,
        DataStatus status,
        Pageable pageable);

    Page<ProductsEntity> findAllByCreator(CreatorEntity creatorEntity, Pageable pageable);

    Page<ProductsEntity> findAllByCreatorAndStatus(CreatorEntity creatorEntity, DataStatus status,
        Pageable pageable);

    Page<HotPickAsset> findNewProductList(PeriodType period, Pageable pageable);

    Page<HotPickAsset> findSaleProductList(PeriodType period, Pageable pageable);

    Page<HotPickAsset> findFreeProductList(PeriodType period, Pageable pageable);

    Page<HotPickAsset> findChargeProductList(PeriodType period, Pageable pageable);

    void save(ProductsEntity createdProductsEntity);

    long countAllByCreatorEntity(CreatorEntity creatorEntity);

    Page<ProductsEntity> findAllDistinctByTagListAndStatus(List<String> tagList, DataStatus status,
        Pageable pageable);

    Page<ProductsEntity> findAllByStatusWithPriceOption(DataStatus dataStatus,
        List<Integer> priceOption, Pageable pageable);


    Page<ProductsEntity> findAllDistinctByTagListAndStatusWithPriceOption(List<String> tagList,
        DataStatus dataStatus, List<Integer> priceOption, Pageable pageable);

    Page<ProductsEntity> findAllByStatus(DataStatus status, Pageable pageable);

    Page<ProductsEntity> findAllAdminProductsList(Pageable pageable);

    Long countAllProducts();
}
