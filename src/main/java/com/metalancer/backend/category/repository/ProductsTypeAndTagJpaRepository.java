package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.category.entity.ProductsTypeAndTagEntity;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsTypeAndTagJpaRepository extends JpaRepository<ProductsTypeAndTagEntity, Long> {
    List<ProductsTypeAndTagEntity> findAllByGenreGalaxyTypeEntity(GenreGalaxyTypeEntity genreGalaxyTypeEntity);

    List<ProductsTypeAndTagEntity> findAllByTrendSpotlightTypeEntity(TrendSpotlightTypeEntity trendSpotlightTypeEntity);
}
