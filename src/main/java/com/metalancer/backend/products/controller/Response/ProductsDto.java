package com.metalancer.backend.products.controller.Response;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.common.constants.ProperAssetType;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.domain.ProperAsset;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ProductsDto {

    @Data
    public static class HotPickResponse {

        private HotPickType hotPickType;
        private Page<HotPickAsset> hotPickAssetList;
    }

    @Data
    public static class ProperAssetResponse {

        private ProperAssetType properAssetType;
        private Page<ProperAsset> properAssetList;
    }
}
