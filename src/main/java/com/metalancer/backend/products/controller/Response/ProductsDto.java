package com.metalancer.backend.products.controller.Response;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.ProperAssetType;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.domain.ProperAsset;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ProductsDto {

    @Data
    @AllArgsConstructor
    public static class HotPickResponse {

        private final HotPickType hotPickType;
        private final Page<HotPickAsset> hotPickAssetList;
    }

    @Data
    @AllArgsConstructor
    public static class ProperAssetResponse {

        private final ProperAssetType properAssetType;
        private final Page<ProperAsset> properAssetList;
    }
}
