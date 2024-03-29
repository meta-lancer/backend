package com.metalancer.backend.category.service;

import com.metalancer.backend.category.dto.CategoryDTO.MainCategory;
import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.category.dto.CategoryDTO.TrendSpotlightCategory;
import com.metalancer.backend.category.entity.TagsEntity;
import com.metalancer.backend.category.repository.GenreGalaxyTypeRepository;
import com.metalancer.backend.category.repository.HotPickTypeRepository;
import com.metalancer.backend.category.repository.ProductsRequestTypeRepository;
import com.metalancer.backend.category.repository.TagsRepository;
import com.metalancer.backend.category.repository.TrendSpotlightTypeRepository;
import com.metalancer.backend.common.exception.BaseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class CategoryListServiceImpl implements CategoryListService {

    private final HotPickTypeRepository hotPickTypeRepository;
    private final TrendSpotlightTypeRepository trendSpotlightTypeRepository;
    private final GenreGalaxyTypeRepository genreGalaxyTypeRepository;
    private final ProductsRequestTypeRepository productsRequestTypeRepository;
    private final TagsRepository tagsRepository;

    @Override
    public List<MainCategory> getHotPickCategoryList() {
        return hotPickTypeRepository.getHotPickCategoryListWithUseYnTrue();
    }

    @Override
    public List<TrendSpotlightCategory> getTrendSpotlightCategoryList() {
        return trendSpotlightTypeRepository.getTrendSpotlightCategoryListWithUseYnTrue();
    }

    @Override
    public List<MainCategory> getGenreGalaxyCategoryList() {
        return genreGalaxyTypeRepository.getGenreGalaxyCategoryListWithUseYnTrue();
    }

    @Override
    public List<RequestCategory> getRequestCategoryList() {
        return productsRequestTypeRepository.getRequestCategoryListWithUseYn();
    }

    @Override
    public List<String> getTagRegisterRecommendList(String keyword) {
        return tagsRepository.findAllByKeywordLimit10(keyword);
    }

    @Override
    public List<MainCategory> getFilterCategoryList() {
        return tagsRepository.findAllMainCategoryByParentsTagName("카테고리").stream()
            .map(TagsEntity::toMainCategory).toList();
    }

    @Override
    public List<MainCategory> getFilterPlatformList() {
        return tagsRepository.findAllMainCategoryByParentsTagName("플랫폼").stream()
            .map(TagsEntity::toMainCategory).toList();
    }

    @Override
    public List<MainCategory> getGenreGalaxyCategoryListWithOutAll() {
        return genreGalaxyTypeRepository.getGenreGalaxyCategoryListWithOutAll();
    }
}