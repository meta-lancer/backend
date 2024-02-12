package com.metalancer.backend.category.repository;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.CreateCategory;
import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import java.util.List;

public interface GenreGalaxyTypeRepository {

    List<CategoryList> getGenreGalaxyCategoryList();

    List<MainCategory> getGenreGalaxyCategoryListWithUseYnTrue();

    GenreGalaxyTypeEntity findByName(String type);

    List<MainCategory> getGenreGalaxyCategoryListWithOutAll();

    void updateCategoryUseYn(Long categoryId);

    void createCategory(CreateCategory dto);
}
