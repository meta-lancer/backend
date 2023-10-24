package com.metalancer.backend.category.service;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.category.dto.CategoryDTO.TrendSpotlightCategory;
import java.util.List;

public interface CategoryListService {


    List<MainCategory> getHotPickCategoryList();

    List<TrendSpotlightCategory> getTrendSpotlightCategoryList();

    List<MainCategory> getGenreGalaxyCategoryList();

    List<RequestCategory> getRequestCategoryList();

    List<String> getTagRegisterRecommendList(String keyword);
}