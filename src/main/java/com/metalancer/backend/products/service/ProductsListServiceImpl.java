package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.products.controller.Response.ProductsDto.GenreGalaxyResponse;
import com.metalancer.backend.products.controller.Response.ProductsDto.HotPickResponse;
import com.metalancer.backend.products.controller.Response.ProductsDto.TrendSpotlightResponse;
import com.metalancer.backend.products.domain.Asset;
import com.metalancer.backend.products.domain.FilterAsset;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.entity.ProductsEntity;
import com.metalancer.backend.products.repository.ProductsRepository;
import com.metalancer.backend.products.repository.ProductsTagRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, BaseException.class})
public class ProductsListServiceImpl implements ProductsListService {

    private final ProductsRepository productsRepository;
    private final ProductsTagRepository productsTagRepository;

    @Override
    public HotPickResponse getHotPickList(HotPickType type, PeriodType period, Pageable pageable) {
        Page<HotPickAsset> hotPickAssets = new PageImpl<>(new ArrayList<>(), pageable, 0);
        switch (type) {
            case NEW -> {
                hotPickAssets = productsRepository.findNewProductList(pageable);
            }
            case SALE -> {
                hotPickAssets = productsRepository.findSaleProductList(pageable);
            }
            case FREE -> {
                hotPickAssets = productsRepository.findFreeProductList(period, pageable);
            }
            case CHARGE -> {
                hotPickAssets = productsRepository.findChargeProductList(period, pageable);
            }
        }
        if (hotPickAssets.getContent().size() > 0) {
            for (Asset hotPickAsset : hotPickAssets) {
                setTagList(hotPickAsset);
            }
        }
        return new HotPickResponse(type, hotPickAssets);
    }

    private void setTagList(Asset hotPickAsset) {
        ProductsEntity productsEntity = productsRepository.findProductById(
            hotPickAsset.getAssetId());
        List<String> tagList = productsTagRepository.findTagListByProduct(productsEntity);
        hotPickAsset.setTagList(tagList);
    }

    @Override
    public GenreGalaxyResponse getGenreGalaxyList(String type, Pageable pageable) {
        return null;
    }

    @Override
    public Page<FilterAsset> getFilterAssetList(Integer sortOption, List<Integer> typeOption,
        List<Integer> genreOption, List<Integer> priceOption, Pageable adjustedPageable) {
        return null;
    }

    @Override
    public TrendSpotlightResponse getTrendSpotlight(String type, Pageable adjustedPageable) {
        return null;
    }
}