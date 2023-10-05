package com.metalancer.backend.category.service;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import java.util.List;

public interface CategoryListService {


    List<MainCategory> getHotPickCategoryList();

    List<MainCategory> getTrendSpotlightCategoryList();

    List<MainCategory> getGenreGalaxyCategoryList();

    List<RequestCategory> getRequestCategoryList();
}