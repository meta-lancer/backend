package com.metalancer.backend.category.service;

import com.metalancer.backend.category.repository.GenreGalaxyTypeRepository;
import com.metalancer.backend.category.repository.HotPickTypeRepository;
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

    @Override
    public List<String> getHotPickCategoryList() {
        return hotPickTypeRepository.getHotPickCategoryList();
    }

    @Override
    public List<String> getTrendSpotlightCategoryList() {

        return trendSpotlightTypeRepository.getTrendSpotlightCategoryList();
    }

    @Override
    public List<String> getGenreGalaxyCategoryList() {

        return genreGalaxyTypeRepository.getGenreGalaxyCategoryList();
    }
}