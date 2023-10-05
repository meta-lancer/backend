package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import java.util.List;

public interface ProductsRequestTypeRepository {

    List<RequestCategory> getRequestCategoryList();
}
