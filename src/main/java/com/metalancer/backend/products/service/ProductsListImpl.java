package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.ProperAssetType;
import com.metalancer.backend.products.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductsListImpl implements ProductsListService {

    private final ProductsRepository productsRepository;

    @Override
    public String getHotPickList(HotPickType type, Pageable pageable) {
        return null;
    }

    @Override
    public String getProperAssetList(ProperAssetType type, Pageable pageable) {
        return null;
    }
}