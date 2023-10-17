package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsTagEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsTagRepositoryImpl implements ProductsTagRepository {

    private final ProductsTagJpaRepository productsTagJpaRepository;

    @Override
    public List<String> findTagListByProduct(ProductsEntity products) {
        return productsTagJpaRepository.findAllByProductsEntityOrderByNameAsc(products).stream()
            .map(
                ProductsTagEntity::getName).collect(Collectors.toList());
    }

    @Override
    public List<ProductsTagEntity> saveAll(List<ProductsTagEntity> productsTagEntityList) {
        return productsTagJpaRepository.saveAll(productsTagEntityList);
    }
}
