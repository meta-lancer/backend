package com.metalancer.backend.products.repository;

import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.entity.ProductsThumbnailEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductsThumbnailRepositoryImpl implements ProductsThumbnailRepository {

    private final ProductsThumbnailJpaRepository productsThumbnailJpaRepository;

    @Override
    public void saveAll(List<ProductsThumbnailEntity> productsThumbnailEntities) {
        productsThumbnailJpaRepository.saveAll(productsThumbnailEntities);
    }

    @Override
    public List<String> findAllUrlByProduct(ProductsEntity productsEntity) {
        List<ProductsThumbnailEntity> productsThumbnailEntities = productsThumbnailJpaRepository.findAllByProductsEntityOrderByThumbnailOrdAsc(
            productsEntity);
        return productsThumbnailEntities.stream().map(ProductsThumbnailEntity::getThumbnailUrl)
            .collect(
                Collectors.toList());
    }

    @Override
    public void deleteAllUrlByProduct(ProductsEntity productsEntity) {
        List<ProductsThumbnailEntity> productsThumbnailEntities = productsThumbnailJpaRepository.findAllByProductsEntity(
            productsEntity);
        productsThumbnailJpaRepository.deleteAll(productsThumbnailEntities);
    }
}
