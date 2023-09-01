package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.NotFoundException;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.entity.Products;
import com.metalancer.backend.users.entity.Creator;
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
    public Products findProductById(Long productsId) {
        return productsJpaRepository.findById(productsId).orElseThrow(
            () -> new NotFoundException(ErrorCode.NOT_FOUND)
        );
    }

    @Override
    public Products findProductByIdAndStatus(Long productsId, DataStatus status) {
        Optional<Products> foundProducts = productsJpaRepository.findById(productsId);
        if (foundProducts.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }
        Products product = foundProducts.get();
        product.isProductsStatusEqualsActive();
        return product;
    }

    @Override
    public Page<Products> findProductsListByCreator(Creator creator, Pageable pageable) {
        return productsJpaRepository.findAllByCreator(creator, pageable);
    }

    @Override
    public Page<Products> findProductsListByCreatorAndStatus(Creator creator, DataStatus status,
        Pageable pageable) {
        return productsJpaRepository.findAllByCreatorAndStatus(creator, status, pageable);
    }

    public Page<Products> findAllByCreator(Creator creator, Pageable pageable) {
        return productsJpaRepository.findAllByCreator(creator, pageable);
    }

    @Override
    public Page<Products> findAllByCreatorAndStatus(Creator creator, DataStatus status,
        Pageable pageable) {
        return productsJpaRepository.findAllByCreatorAndStatus(creator, status, pageable);
    }

    @Override
    public Page<HotPickAsset> findNewProductList(Pageable pageable) {
        return productsJpaRepository.findAllByOrderByCreatedAtDesc(pageable)
            .map(Products::toHotPickAsset);
    }

    @Override
    public Page<HotPickAsset> findSaleProductList(Pageable pageable) {
        return null;
    }

    @Override
    public Page<HotPickAsset> findFreeProductList(PeriodType period, Pageable pageable) {
        return productsJpaRepository.findAllByPriceOrderByViewCntDesc(0, pageable)
            .map(Products::toHotPickAsset);
    }

    @Override
    public Page<HotPickAsset> findChargeProductList(PeriodType period, Pageable pageable) {
        return productsJpaRepository.findAllByPriceIsGreaterThanOrderByViewCntDesc(0, pageable)
            .map(Products::toHotPickAsset);
    }

    ;
}
