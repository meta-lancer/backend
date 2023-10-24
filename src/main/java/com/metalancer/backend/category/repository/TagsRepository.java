package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import java.util.List;

public interface TagsRepository {

    List<String> findAllByTrendSpotLight(TrendSpotlightTypeEntity trendSpotlightTypeEntity);

    List<String> findAllByGenreGalaxy(GenreGalaxyTypeEntity genreGalaxyTypeEntity);

    List<String> findAllTrendSpotLightTags();

    List<String> findAllGenreGalaxyTags();
}
