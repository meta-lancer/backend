package com.metalancer.backend.admin.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AdminCategoryDTO {

    @Data
    @NoArgsConstructor
    public static class CategoryList {

        private Long categoryId;
        private String name;
        private String nameKor;
        private boolean useYn;

        @Builder
        public CategoryList(Long categoryId, String name, String nameKor, boolean useYn) {
            this.categoryId = categoryId;
            this.name = name;
            this.nameKor = nameKor;
            this.useYn = useYn;
        }
    }

    @Data
    @NoArgsConstructor
    public static class TrendSpotlightCategory {

        private Long categoryId;
        private String name;
        private String nameKor;
        private String thumbnail;
        private boolean useYn;

        @Builder
        public TrendSpotlightCategory(Long categoryId, String name, String nameKor,
            String thumbnail,
            boolean useYn) {
            this.categoryId = categoryId;
            this.name = name;
            this.nameKor = nameKor;
            this.thumbnail = thumbnail;
            this.useYn = useYn;
        }
    }
}