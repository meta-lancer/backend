package com.metalancer.backend.products.service;

import com.metalancer.backend.category.entity.GenreGalaxyTypeEntity;
import com.metalancer.backend.category.entity.TrendSpotlightTypeEntity;
import com.metalancer.backend.category.repository.GenreGalaxyTypeRepository;
import com.metalancer.backend.category.repository.TagsRepository;
import com.metalancer.backend.category.repository.TrendSpotlightTypeRepository;
import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.exception.BaseException;
import com.metalancer.backend.products.domain.Asset;
import com.metalancer.backend.products.domain.FilterAsset;
import com.metalancer.backend.products.domain.GenreGalaxy;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.domain.TrendSpotlight;
import com.metalancer.backend.products.dto.ProductsDto.GenreGalaxyResponse;
import com.metalancer.backend.products.dto.ProductsDto.HotPickResponse;
import com.metalancer.backend.products.dto.ProductsDto.TrendSpotlightResponse;
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
    private final TagsRepository tagsRepository;
    private final GenreGalaxyTypeRepository genreGalaxyTypeRepository;
    private final TrendSpotlightTypeRepository trendSpotlightTypeRepository;

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
        return HotPickResponse.builder().hotPickType(type).hotPickAssetList(hotPickAssets).build();
    }

    private void setTagList(Asset hotPickAsset) {
        ProductsEntity productsEntity = productsRepository.findProductById(
            hotPickAsset.getProductsId());
        List<String> tagList = productsTagRepository.findTagListByProduct(productsEntity);
        hotPickAsset.setTagList(tagList);
    }

    @Override
    public TrendSpotlightResponse getTrendSpotlight(String platformType,
        Pageable pageable) {
        List<String> tagList = null;
        if (platformType.equals("all") || platformType.equals("ALL")) {
            tagList = tagsRepository.findAllTrendSpotLightTags();
        } else {
            TrendSpotlightTypeEntity trendSpotlightTypeEntity = trendSpotlightTypeRepository.findByName(
                platformType);
            tagList = tagsRepository.findAllByTrendSpotLight(trendSpotlightTypeEntity);
        }

        Page<ProductsEntity> productsEntities = productsRepository.findAllDistinctByTagListAndStatus(
            tagList, DataStatus.ACTIVE, pageable);
        Page<TrendSpotlight> genreGalaxies = productsEntities.map(ProductsEntity::toTrendSpotLight);
        return TrendSpotlightResponse.builder().trendSpotlightType(platformType)
            .trendSpotlightList(genreGalaxies).build();
    }

    @Override
    public GenreGalaxyResponse getGenreGalaxyList(String type, Pageable pageable) {
        List<String> tagList = null;
        if (type.equals("all") || type.equals("ALL")) {
            tagList = tagsRepository.findAllGenreGalaxyTags();
        } else {
            GenreGalaxyTypeEntity genreGalaxyTypeEntity = genreGalaxyTypeRepository.findByName(
                type);
            tagList = tagsRepository.findAllByGenreGalaxy(genreGalaxyTypeEntity);
        }
        Page<ProductsEntity> productsEntities = productsRepository.findAllDistinctByTagListAndStatus(
            tagList, DataStatus.ACTIVE, pageable);
        Page<GenreGalaxy> genreGalaxies = productsEntities.map(ProductsEntity::toGenreGalaxy);
        return GenreGalaxyResponse.builder().genreGalaxyType(type).genreGalaxyList(genreGalaxies)
            .build();
    }

    @Override
    public Page<FilterAsset> getFilterAssetList(List<String> categoryOption,
        List<String> trendOption, List<Integer> priceOption,
        Pageable pageable) {
        List<String> tagList = new ArrayList<>();
        if (isNullOrEmpty(categoryOption) && isNullOrEmpty(trendOption)) {
            Page<ProductsEntity> productsEntities =
                priceOption == null || priceOption.size() == 0 ?
                    productsRepository.findAllByStatus(DataStatus.ACTIVE, pageable) :
                    productsRepository.findAllByStatusWithPriceOption(DataStatus.ACTIVE,
                        priceOption, pageable);
            return productsEntities.map(ProductsEntity::toFilterAsset);
        }

        if (!isNullOrEmpty(categoryOption)) {
            setTagListByOption(categoryOption, tagList);
        }
        if (!isNullOrEmpty(trendOption)) {
            setTagListByOption(trendOption, tagList);
        }

        // tagList를 가져온다.
        Page<ProductsEntity> productsEntities =
            priceOption == null || priceOption.size() == 0 ?
                productsRepository.findAllDistinctByTagListAndStatus(
                    tagList, DataStatus.ACTIVE, pageable)
                : productsRepository.findAllDistinctByTagListAndStatusWithPriceOption(
                    tagList, DataStatus.ACTIVE, priceOption, pageable);

        return productsEntities.map(ProductsEntity::toFilterAsset);
    }

    private void setTagListByOption(List<String> categoryOption, List<String> tagList) {
        for (String category : categoryOption) {
            String categoryTag = tagsRepository.findStringByTagName(category);
            List<String> categoryTagList = tagsRepository.findAllByParentsTagName(category);
            tagList.add(categoryTag);
            tagList.addAll(categoryTagList);
        }
    }

    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}