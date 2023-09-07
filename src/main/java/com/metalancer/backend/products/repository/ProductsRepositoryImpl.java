package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepository {

    private final ProductsJpaRepository productsJpaRepository;

    private final String product = "상품";

    @Override
    public ProductsEntity findProductById(Long productsId) {
        return productsJpaRepository.findById(productsId).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public ProductsEntity findProductByIdAndStatus(Long productsId, DataStatus status) {
        Optional<ProductsEntity> foundProducts = productsJpaRepository.findById(productsId);
        if (foundProducts.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        ProductsEntity product = foundProducts.get();
        product.isProductsStatusEqualsActive();
        return product;
    }

    @Override
    public Page<ProductsEntity> findProductsListByCreator(CreatorEntity creatorEntity,
        Pageable pageable) {
        return productsJpaRepository.findAllByCreator(creatorEntity, pageable);
    }

    @Override
    public Page<ProductsEntity> findProductsListByCreatorAndStatus(CreatorEntity creatorEntity,
        DataStatus status,
        Pageable pageable) {
        return productsJpaRepository.findAllByCreatorAndStatus(creatorEntity, status, pageable);
    }

    public Page<ProductsEntity> findAllByCreator(CreatorEntity creatorEntity, Pageable pageable) {
        return productsJpaRepository.findAllByCreator(creatorEntity, pageable);
    }

    @Override
    public Page<ProductsEntity> findAllByCreatorAndStatus(CreatorEntity creatorEntity,
        DataStatus status,
        Pageable pageable) {
        return productsJpaRepository.findAllByCreatorAndStatus(creatorEntity, status, pageable);
    }

    @Override
    public Page<HotPickAsset> findNewProductList(Pageable pageable) {
        return productsJpaRepository.findAllByStatusOrderByCreatedAtDesc(DataStatus.ACTIVE,
                pageable)
            .map(ProductsEntity::toHotPickAsset);
    }

    @Override
    public Page<HotPickAsset> findSaleProductList(Pageable pageable) {
        return null;
    }

    @Override
    public Page<HotPickAsset> findFreeProductList(PeriodType period, Pageable pageable) {
        return productsJpaRepository.findAllByPriceAndStatusOrderByViewCntDesc(0, DataStatus.ACTIVE,
                pageable)
            .map(ProductsEntity::toHotPickAsset);
    }

    @Override
    public Page<HotPickAsset> findChargeProductList(PeriodType period, Pageable pageable) {
        return productsJpaRepository.findAllByPriceIsGreaterThanAndStatusOrderByViewCntDesc(0,
                DataStatus.ACTIVE, pageable)
            .map(ProductsEntity::toHotPickAsset);
    }

    ;
}
