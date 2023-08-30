package com.metalancer.backend.products.service;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.ProperAssetType;
import org.springframework.data.domain.Pageable;

public interface ProductsListService {

    String getHotPickList(HotPickType type, Pageable pageable);

    String getProperAssetList(ProperAssetType type, Pageable pageable);
}