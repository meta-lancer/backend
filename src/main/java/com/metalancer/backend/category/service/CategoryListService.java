package com.metalancer.backend.category.service;

import java.util.List;

public interface CategoryListService {


    List<String> getHotPickCategoryList();

    List<String> getTrendSpotlightCategoryList();

    List<String> getGenreGalaxyCategoryList();
}