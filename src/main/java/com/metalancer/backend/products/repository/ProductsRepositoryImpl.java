package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.users.entity.CreatorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public ProductsEntity findProductBySharedLinkAndStatus(String sharedLink, DataStatus status) {
        Optional<ProductsEntity> productsEntity = productsJpaRepository.findBySharedLinkContains(
                sharedLink);
        if (productsEntity.isEmpty()) {
            throw new BaseException(ErrorCode.NOT_FOUND);
        }
        ProductsEntity foundProductsEntity = productsEntity.get();
        if (!foundProductsEntity.getStatus().equals(DataStatus.ACTIVE)) {
            throw new BaseException(ErrorCode.PRODUCTS_STATUS_ERROR);
        }
        return foundProductsEntity;
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
        return productsJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity, pageable,
                DataStatus.ACTIVE);
    }

    @Override
    public Page<ProductsEntity> findProductsListByCreatorAndStatus(CreatorEntity creatorEntity,
                                                                   DataStatus status,
                                                                   Pageable pageable) {
        return productsJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity, status,
                pageable);
    }

    public Page<ProductsEntity> findAllByCreator(CreatorEntity creatorEntity, Pageable pageable) {
        return productsJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity, pageable,
                DataStatus.ACTIVE);
    }

    @Override
    public Page<ProductsEntity> findAllByCreatorAndStatus(CreatorEntity creatorEntity,
                                                          DataStatus status,
                                                          Pageable pageable) {
        return productsJpaRepository.findAllByCreatorEntityAndStatus(creatorEntity, status,
                pageable);
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
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = null;
        if (period.equals(PeriodType.WEEKLY)) {
            startDateTime = endDateTime.minusWeeks(1);
        } else {
            startDateTime = endDateTime.minusMonths(1);
        }
        return productsJpaRepository.findAllByPriceAndStatusAndCreatedAtBetweenOrderByViewCntDesc(0, DataStatus.ACTIVE,
                        startDateTime, endDateTime, pageable)
                .map(ProductsEntity::toHotPickAsset);
    }

    @Override
    public Page<HotPickAsset> findChargeProductList(PeriodType period, Pageable pageable) {
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = null;
        if (period.equals(PeriodType.WEEKLY)) {
            startDateTime = endDateTime.minusWeeks(1);
        } else {
            startDateTime = endDateTime.minusMonths(1);
        }
        return productsJpaRepository.findAllByPriceIsGreaterThanAndStatusAndCreatedAtBetweenOrderByViewCntDesc(0,
                        DataStatus.ACTIVE,
                        startDateTime,
                        endDateTime,
                        pageable)
                .map(ProductsEntity::toHotPickAsset);
    }

    @Override
    public void save(ProductsEntity createdProductsEntity) {
        productsJpaRepository.save(createdProductsEntity);
    }

    @Override
    public long countAllByCreatorEntity(CreatorEntity creatorEntity) {
        return productsJpaRepository.countAllByCreatorEntity(creatorEntity);
    }

    ;
}
