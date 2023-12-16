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
import com.metalancer.backend.products.repository.ProductsThumbnailRepository;
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
    private final ProductsThumbnailRepository productsThumbnailRepository;

    @Override
    public HotPickResponse getHotPickList(HotPickType type, PeriodType period, Pageable pageable) {
        Page<HotPickAsset> hotPickAssets = new PageImpl<>(new ArrayList<>(), pageable, 0);
        switch (type) {
            case NEW -> {
                hotPickAssets = productsRepository.findNewProductList(period, pageable);
            }
            case SALE -> {
                hotPickAssets = productsRepository.findSaleProductList(period, pageable);
            }
            case FREE -> {
                hotPickAssets = productsRepository.findFreeProductList(period, pageable);
            }
            case CHARGE -> {
                hotPickAssets = productsRepository.findChargeProductList(period, pageable);
            }
        }
        if (hotPickAssets != null && hotPickAssets.getContent().size() > 0) {
            for (Asset hotPickAsset : hotPickAssets) {
                ProductsEntity productsEntity = productsRepository.findProductById(
                    hotPickAsset.getProductsId());
                setTagList(productsEntity, hotPickAsset);
                setThumbnail(hotPickAsset, productsEntity);
            }
        }

        return HotPickResponse.builder().hotPickType(type).hotPickAssetList(hotPickAssets).build();
    }

    private void setThumbnail(Asset hotPickAsset, ProductsEntity productsEntity) {
        List<String> thumbnailEntityList = productsThumbnailRepository.findAllUrlByProduct(
            productsEntity);
        hotPickAsset.setThumbnail(thumbnailEntityList.size() > 0 ? thumbnailEntityList.get(0) : "");
    }

    private void setTagList(ProductsEntity productsEntity, Asset hotPickAsset) {
        List<String> tagList = productsTagRepository.findTagListByProduct(productsEntity);
        hotPickAsset.setTagList(tagList);
    }

    @Override
    public TrendSpotlightResponse getTrendSpotlight(String platformType,
        Pageable pageable) {
        List<String> tagList = null;
        if (platformType.isBlank() || platformType.equals("all") || platformType.equals("ALL")) {
            tagList = tagsRepository.findAllTrendSpotLightTags();
        } else {
            TrendSpotlightTypeEntity trendSpotlightTypeEntity = trendSpotlightTypeRepository.findByName(
                platformType);
            tagList = tagsRepository.findAllByTrendSpotLight(trendSpotlightTypeEntity);
        }

        Page<ProductsEntity> productsEntities = productsRepository.findAllDistinctByTagListAndStatus(
            tagList, DataStatus.ACTIVE, pageable);
        Page<TrendSpotlight> trendSpotlights = productsEntities.map(
            ProductsEntity::toTrendSpotLight);
        if (trendSpotlights.getContent().size() > 0) {
            for (Asset trendSpotlight : trendSpotlights) {
                ProductsEntity productsEntity = productsRepository.findProductById(
                    trendSpotlight.getProductsId());
                setTagList(productsEntity, trendSpotlight);
                setThumbnail(trendSpotlight, productsEntity);
            }
        }
        return TrendSpotlightResponse.builder().trendSpotlightType(platformType)
            .trendSpotlightList(trendSpotlights).build();
    }

    @Override
    public GenreGalaxyResponse getGenreGalaxyList(String type, Pageable pageable) {
        List<String> tagList = null;
        if (type.isBlank() || type.equals("all") || type.equals("ALL")) {
            tagList = tagsRepository.findAllGenreGalaxyTags();
        } else {
            GenreGalaxyTypeEntity genreGalaxyTypeEntity = genreGalaxyTypeRepository.findByName(
                type);
            // 혹시 모르니 대문자로 했을떄도!
            if (genreGalaxyTypeEntity == null) {
                type = type.toUpperCase();
                genreGalaxyTypeEntity = genreGalaxyTypeRepository.findByName(
                    type);
            }
            tagList = tagsRepository.findAllByGenreGalaxy(genreGalaxyTypeEntity);
        }

        Page<ProductsEntity> productsEntities = productsRepository.findAllDistinctByTagListAndStatus(
            tagList, DataStatus.ACTIVE, pageable);
        Page<GenreGalaxy> genreGalaxies = productsEntities.map(ProductsEntity::toGenreGalaxy);
        if (genreGalaxies.getContent().size() > 0) {
            for (Asset genreGalaxyAsset : genreGalaxies) {
                ProductsEntity productsEntity = productsRepository.findProductById(
                    genreGalaxyAsset.getProductsId());
                setTagList(productsEntity, genreGalaxyAsset);
                setThumbnail(genreGalaxyAsset, productsEntity);
            }
        }
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

            Page<FilterAsset> response = productsEntities.map(ProductsEntity::toFilterAsset);
            setProductsTagList(response);

            return response;
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

        Page<FilterAsset> response = productsEntities.map(ProductsEntity::toFilterAsset);

        setProductsTagList(response);

        return response;
    }

    @Override
    public Page<FilterAsset> getFilterAssetListWithKeyword(List<String> categoryOption,
        List<String> trendOption, List<Integer> priceOption, String keyword,
        Pageable pageable) {
        List<String> tagList = new ArrayList<>();
        if (isNullOrEmpty(categoryOption) && isNullOrEmpty(trendOption)) {
            Page<ProductsEntity> productsEntities =
                keyword != null && !keyword.isEmpty() ? (
                    priceOption == null || priceOption.isEmpty() ?
                        productsRepository.findAllByStatusWithKeyword(DataStatus.ACTIVE, keyword,
                            pageable) :
                        productsRepository.findAllByStatusWithKeywordAndPriceOption(
                            DataStatus.ACTIVE,
                            priceOption, keyword, pageable))
                    : priceOption == null || priceOption.isEmpty() ?
                        productsRepository.findAllByStatus(DataStatus.ACTIVE, pageable) :
                        productsRepository.findAllByStatusWithPriceOption(DataStatus.ACTIVE,
                            priceOption, pageable);

            Page<FilterAsset> response = productsEntities.map(ProductsEntity::toFilterAsset);
            setProductsTagList(response);

            return response;
        }

        if (!categoryOption.contains("전체") && !isNullOrEmpty(categoryOption)) {
            setTagListByOption(categoryOption, tagList);
        }
        if (!isNullOrEmpty(trendOption)) {
            setTagListByOption(trendOption, tagList);
        }

        // tagList를 가져온다.
        Page<ProductsEntity> productsEntities =
            keyword != null && !keyword.isEmpty() ? (
                priceOption == null || priceOption.isEmpty() ?
                    productsRepository.findAllDistinctByTagListAndKeywordAndStatus(
                        tagList, DataStatus.ACTIVE, keyword, pageable)
                    : productsRepository.findAllDistinctByTagListAndKeywordAndStatusWithPriceOption(
                        tagList, DataStatus.ACTIVE, priceOption, keyword, pageable))
                :
                    priceOption == null || priceOption.isEmpty() ?
                        productsRepository.findAllDistinctByTagListAndStatus(
                            tagList, DataStatus.ACTIVE, pageable)
                        : productsRepository.findAllDistinctByTagListAndStatusWithPriceOption(
                            tagList, DataStatus.ACTIVE, priceOption, pageable);

        Page<FilterAsset> response = productsEntities.map(ProductsEntity::toFilterAsset);

        setProductsTagList(response);

        return response;
    }

    private void setProductsTagList(Page<FilterAsset> response) {
        if (response.getContent().size() > 0) {
            for (Asset filterAsset : response) {
                ProductsEntity productsEntity = productsRepository.findProductById(
                    filterAsset.getProductsId());
                setTagList(productsEntity, filterAsset);
                setThumbnail(filterAsset, productsEntity);
            }
        }
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