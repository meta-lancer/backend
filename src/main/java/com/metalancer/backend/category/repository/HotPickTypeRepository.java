package com.metalancer.backend.category.repository;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.CreateCategory;
import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import java.util.List;

public interface HotPickTypeRepository {

    List<CategoryList> getHotPickCategoryList();

    List<MainCategory> getHotPickCategoryListWithUseYnTrue();

    void updateCategoryUseYn(Long categoryId);

    void createCategory(CreateCategory dto);
}
