package com.metalancer.backend.creators.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreatorRequestDTO {

    @Data
    @NoArgsConstructor
    public static class AssetRequest {

        private String productsCategory;
        private String title;
        private int price;
//        private String productionProgram;
//        private String compatibleProgram;
//        private List<String> tagList;
//        private String assetDetail;
//        private String assetNotice;
//        private String assetCopyRight;
//        private String website;
    }

}
