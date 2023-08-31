package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.PeriodType;
import com.metalancer.backend.common.constants.ProperAssetType;
import com.metalancer.backend.products.controller.Response.ProductsDto.HotPickResponse;
import com.metalancer.backend.products.controller.Response.ProductsDto.ProperAssetResponse;
import com.metalancer.backend.products.domain.FilterAsset;
import com.metalancer.backend.products.repository.ProductsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsListImpl implements ProductsListService {

    private final ProductsRepository productsRepository;

    @Override
    public HotPickResponse getHotPickList(HotPickType type, PeriodType period, Pageable pageable) {
        return null;
    }

    @Override
    public ProperAssetResponse getProperAssetList(ProperAssetType type, Pageable pageable) {
        return null;
    }

    @Override
    public Page<FilterAsset> getFilterAssetList(Integer sortOption, List<Integer> typeOption,
        List<Integer> genreOption, List<Integer> priceOption, Pageable adjustedPageable) {
        return null;
    }
}