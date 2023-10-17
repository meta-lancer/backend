package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.TrendSpotlightCategory;
import java.util.List;

public interface TrendSpotlightTypeRepository {

    List<TrendSpotlightCategory> getTrendSpotlightCategoryList();
}
