package com.metalancer.backend.category.repository;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.CreateCategory;
import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import java.util.List;

public interface ProductsRequestTypeRepository {

    List<CategoryList> getRequestCategoryList();

    List<RequestCategory> getRequestCategoryListWithUseYn();

    void updateCategoryUseYn(Long categoryId);

    void createCategory(CreateCategory dto);
}
