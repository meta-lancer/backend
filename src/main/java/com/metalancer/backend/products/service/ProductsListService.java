package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.constants.ProperAssetType;
import com.metalancer.backend.products.controller.Response.ProductsDto.HotPickResponse;
import com.metalancer.backend.products.controller.Response.ProductsDto.ProperAssetResponse;
import com.metalancer.backend.products.domain.FilterAsset;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductsListService {

    HotPickResponse getHotPickList(HotPickType type, PeriodType period, Pageable pageable);

    ProperAssetResponse getProperAssetList(ProperAssetType type, Pageable pageable);

    Page<FilterAsset> getFilterAssetList(Integer sortOption, List<Integer> typeOption,
        List<Integer> genreOption, List<Integer> priceOption, Pageable adjustedPageable);
}