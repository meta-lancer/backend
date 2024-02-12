package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.CreateCategory;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.TrendSpotlightCategory;
import com.metalancer.backend.common.constants.CategoryType;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AdminCategoryService {

    List<CategoryList> getAdminHotPickCategoryList();

    List<TrendSpotlightCategory> getAdminTrendSpotlightCategoryList();

    List<CategoryList> getAdminGenreGalaxyCategoryList();

    List<CategoryList> getAdminRequestCategoryList();


    boolean updateCategoryUseYn(CategoryType categoryType, Long categoryId);

    boolean createCategory(CreateCategory dto, MultipartFile thumbnail);
}