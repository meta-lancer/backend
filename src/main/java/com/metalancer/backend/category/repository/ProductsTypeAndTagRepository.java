package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;

import java.util.List;

public interface ProductsTypeAndTagRepository {

    List<String> findAllByGenreGalaxy(GenreGalaxyTypeEntity genreGalaxyTypeEntity);

    List<String> findAllByTrendSpotLight(TrendSpotlightTypeEntity trendSpotlightTypeEntity);
}
