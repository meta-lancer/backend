package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import java.util.List;

public interface TrendSpotlightTypeRepository {

    List<MainCategory> getTrendSpotlightCategoryList();
}
