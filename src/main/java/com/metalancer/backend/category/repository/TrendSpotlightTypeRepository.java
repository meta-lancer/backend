package com.metalancer.backend.category.repository;

import com.metalancer.backend.admin.dto.AdminCategoryDTO;
import com.metalancer.backend.category.dto.CategoryDTO.TrendSpotlightCategory;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import java.util.List;

public interface TrendSpotlightTypeRepository {

    List<AdminCategoryDTO.TrendSpotlightCategory> getTrendSpotlightCategoryList();

    List<TrendSpotlightCategory> getTrendSpotlightCategoryListWithUseYnTrue();

    TrendSpotlightTypeEntity findByName(String platformType);

    void updateCategoryUseYn(Long categoryId);
}
