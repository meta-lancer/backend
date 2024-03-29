package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.TrendSpotlightCategory;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.NotFoundException;
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

    @Override
    public List<TrendSpotlightCategory> getTrendSpotlightCategoryListWithUseYnTrue() {
        return trendSpotlightTypeJpaRepository.findAllByUseYnTrue().stream()
            .map(TrendSpotlightTypeEntity::ToMainCategory).collect(
                Collectors.toList());
    }

    @Override
    public TrendSpotlightTypeEntity findByName(String platformType) {
        return trendSpotlightTypeJpaRepository.findByName(platformType).orElseThrow(
            () -> new NotFoundException("TrendSpotLight: ", ErrorCode.TYPE_NOT_FOUND)
        );
    }
}
