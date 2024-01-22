package com.metalancer.backend.category.repository;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import java.util.List;

public interface HotPickTypeRepository {

    List<MainCategory> getHotPickCategoryList();

    List<MainCategory> getHotPickCategoryListWithUseYnTrue();
}
