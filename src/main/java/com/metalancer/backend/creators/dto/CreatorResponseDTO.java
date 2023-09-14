package com.metalancer.backend.creators.dto;

import com.metalancer.backend.products.domain.ProductsDetail;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreatorResponseDTO {

    @Data
    @NoArgsConstructor
    public static class AssetCreatedResponse {

        ProductsDetail productsDetail;

        @Builder
        public AssetCreatedResponse(ProductsDetail productsDetail) {
            this.productsDetail = productsDetail;
        }
    }

}
