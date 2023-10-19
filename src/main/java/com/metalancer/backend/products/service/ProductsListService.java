package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.products.domain.FilterAsset;
import com.metalancer.backend.products.dto.ProductsDto.GenreGalaxyResponse;
import com.metalancer.backend.products.dto.ProductsDto.HotPickResponse;
import com.metalancer.backend.products.dto.ProductsDto.TrendSpotlightResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsListService {

    HotPickResponse getHotPickList(HotPickType type, PeriodType period, Pageable pageable);

    GenreGalaxyResponse getGenreGalaxyList(String type, Pageable pageable);

    Page<FilterAsset> getFilterAssetList(Integer sortOption, List<Integer> typeOption,
        List<Integer> genreOption, List<Integer> priceOption, Pageable adjustedPageable);

    TrendSpotlightResponse getTrendSpotlight(String platformType, Pageable adjustedPageable);
}