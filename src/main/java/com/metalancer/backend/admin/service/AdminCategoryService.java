package com.metalancer.backend.admin.service;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.category.dto.CategoryDTO.TrendSpotlightCategory;
import java.util.List;

public interface AdminCategoryService {

    List<MainCategory> getAdminHotPickCategoryList();

    List<TrendSpotlightCategory> getAdminTrendSpotlightCategoryList();

    List<MainCategory> getAdminGenreGalaxyCategoryList();

    List<RequestCategory> getAdminRequestCategoryList();
}