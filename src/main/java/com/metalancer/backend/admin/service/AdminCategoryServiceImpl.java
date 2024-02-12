package com.metalancer.backend.admin.service;

import com.metalancer.backend.admin.dto.AdminCategoryDTO.CategoryList;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.CreateCategory;
import com.metalancer.backend.admin.dto.AdminCategoryDTO.TrendSpotlightCategory;
import com.metalancer.backend.category.repository.GenreGalaxyTypeRepository;
import com.metalancer.backend.category.repository.HotPickTypeRepository;
import com.metalancer.backend.category.repository.ProductsRequestTypeRepository;
import com.metalancer.backend.category.repository.TrendSpotlightTypeRepository;
import com.metalancer.backend.common.constants.CategoryType;
import com.metalancer.backend.common.exception.BaseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final HotPickTypeRepository hotPickTypeRepository;
    private final TrendSpotlightTypeRepository trendSpotlightTypeRepository;
    private final GenreGalaxyTypeRepository genreGalaxyTypeRepository;
    private final ProductsRequestTypeRepository productsRequestTypeRepository;

    @Override
    public List<CategoryList> getAdminHotPickCategoryList() {
        return hotPickTypeRepository.getHotPickCategoryList();
    }

    @Override
    public List<TrendSpotlightCategory> getAdminTrendSpotlightCategoryList() {

        return trendSpotlightTypeRepository.getTrendSpotlightCategoryList();
    }

    @Override
    public List<CategoryList> getAdminGenreGalaxyCategoryList() {

        return genreGalaxyTypeRepository.getGenreGalaxyCategoryList();
    }

    @Override
    public List<CategoryList> getAdminRequestCategoryList() {
        return productsRequestTypeRepository.getRequestCategoryList();
    }

    @Override
    public boolean updateCategoryUseYn(CategoryType categoryType, Long categoryId) {
        switch (categoryType) {
            case HOT_PICK -> {
                hotPickTypeRepository.updateCategoryUseYn(categoryId);
            }
            case TREND_SPOTLIGHT -> {
                trendSpotlightTypeRepository.updateCategoryUseYn(categoryId);
            }
            case GENRE_GALAXY -> {
                genreGalaxyTypeRepository.updateCategoryUseYn(categoryId);
            }
            case REQUEST -> {
                productsRequestTypeRepository.updateCategoryUseYn(categoryId);
            }
        }
        return true;
    }

    @Override
    public boolean createCategory(CreateCategory dto, MultipartFile thumbnail) {
        switch (dto.getCategoryType()) {
            case HOT_PICK -> {
                hotPickTypeRepository.createCategory(dto);
            }
            case TREND_SPOTLIGHT -> {
                trendSpotlightTypeRepository.createCategory(dto);
            }
            case GENRE_GALAXY -> {
                genreGalaxyTypeRepository.createCategory(dto);
            }
            case REQUEST -> {
                productsRequestTypeRepository.createCategory(dto);
            }
        }
        return true;
    }
}