package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.TrendSpotlightCategory;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrendSpotlightTypeRepositoryImpl implements TrendSpotlightTypeRepository {

    private final TrendSpotlightTypeJpaRepository trendSpotlightTypeJpaRepository;

    @Override
    public List<TrendSpotlightCategory> getTrendSpotlightCategoryList() {
        return trendSpotlightTypeJpaRepository.findAll().stream()
            .map(TrendSpotlightTypeEntity::ToMainCategory).collect(
                Collectors.toList());
    }
}
