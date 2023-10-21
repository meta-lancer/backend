package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.category.entity.ProductsTypeAndTagEntity;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductsTypeAndTagRepositoryImpl implements ProductsTypeAndTagRepository {

    private final ProductsTypeAndTagJpaRepository productsTypeAndTagJpaRepository;

    @Override
    public List<String> findAllByGenreGalaxy(GenreGalaxyTypeEntity genreGalaxyTypeEntity) {
        return productsTypeAndTagJpaRepository.findAllByGenreGalaxyTypeEntity(genreGalaxyTypeEntity).stream().map(ProductsTypeAndTagEntity::getProductsTagName).toList();
    }

    @Override
    public List<String> findAllByTrendSpotLight(TrendSpotlightTypeEntity trendSpotlightTypeEntity) {
        return productsTypeAndTagJpaRepository.findAllByTrendSpotlightTypeEntity(trendSpotlightTypeEntity).stream().map(ProductsTypeAndTagEntity::getProductsTagName).toList();
    }
}
