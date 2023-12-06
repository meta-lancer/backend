package com.metalancer.backend.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CategoryDTO {

    @Data
    @NoArgsConstructor
    public static class MainCategory {

        private String name;
        private String nameKor;

        @Builder
        public MainCategory(String name, String nameKor) {
            this.name = name;
            this.nameKor = nameKor;
        }
    }

    @Data
    @NoArgsConstructor
    public static class TrendSpotlightCategory {

        private String name;
        private String nameKor;
        private String thumbnail;

        @Builder
        public TrendSpotlightCategory(String name, String nameKor, String thumbnail) {
            this.name = name;
            this.nameKor = nameKor;
            this.thumbnail = thumbnail;
        }
    }

    @Data
    @NoArgsConstructor
    public static class RequestCategory {

        private String name;
        private String nameKor;

        @Builder
        public RequestCategory(String name, String nameKor) {
            this.name = name;
            this.nameKor = nameKor;
        }
    }
}
