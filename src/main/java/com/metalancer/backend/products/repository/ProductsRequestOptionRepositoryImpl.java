package com.metalancer.backend.products.repository;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.products.domain.RequestOption;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsRequestOptionEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsRequestOptionRepositoryImpl implements ProductsRequestOptionRepository {

    private final ProductsRequestOptionJpaRepository productsRequestOptionJpaRepository;

    @Override
    public List<RequestOption> findAllByProducts(ProductsEntity foundProductsEntity) {
        return productsRequestOptionJpaRepository.findAllByProductsEntityAndStatus(
            foundProductsEntity, DataStatus.ACTIVE).stream().map(
            ProductsRequestOptionEntity::toRequestOption).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductsRequestOptionEntity> findOptionById(Long requestOptionId) {
        return productsRequestOptionJpaRepository.findById(requestOptionId);
    }

    @Override
    public Optional<ProductsRequestOptionEntity> findOptionByProductsAndId(
        ProductsEntity productsEntity, Long requestOptionId) {
        return productsRequestOptionJpaRepository.findByProductsEntityAndIdAndStatus(productsEntity,
            requestOptionId, DataStatus.ACTIVE);
    }

    @Override
    public void save(ProductsRequestOptionEntity createdProductsRequestOptionEntity) {
        productsRequestOptionJpaRepository.save(createdProductsRequestOptionEntity);
    }
}
