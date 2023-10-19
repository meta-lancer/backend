package com.metalancer.backend.products.dto;

import com.metalancer.backend.common.constants.HotPickType;
import com.metalancer.backend.products.domain.GenreGalaxy;
import com.metalancer.backend.products.domain.HotPickAsset;
import com.metalancer.backend.products.domain.TrendSpotlight;
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
    public static class GenreGalaxyResponse {

        private final String genreGalaxyType;
        private final Page<GenreGalaxy> genreGalaxyList;
    }

    @Data
    @AllArgsConstructor
    public static class TrendSpotlightResponse {

        private final String trendSpotlightType;
        private final Page<TrendSpotlight> trendSpotlightList;
    }
}
