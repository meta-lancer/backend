package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsViewsEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsViewsRepositoryImpl implements ProductsViewsRepository {

    private final ProductsViewsJpaRepository productsViewsJpaRepository;

    @Override
    public void saveAll(List<ProductsViewsEntity> productsViewsEntities) {
        productsViewsJpaRepository.saveAll(productsViewsEntities);
    }

    @Override
    public List<String> findAllUrlByProduct(ProductsEntity productsEntity) {
        List<ProductsViewsEntity> productsViewsEntities = productsViewsJpaRepository.findAllByProductsEntityOrderByViewOrdAsc(
            productsEntity);
        return productsViewsEntities.stream().map(ProductsViewsEntity::getViewUrl).collect(
            Collectors.toList());
    }
}
