package com.metalancer.backend.creators.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreatorRequestDTO {

    @Data
    @NoArgsConstructor
    public static class AssetRequest {

        @Schema(description = "에셋 이름", example = "")
        private String title;
        @Schema(description = "에셋 가격", example = "")
        private int price;

        @Schema(description = "모델 타입", example = "")
        private String modelType;

        @Schema(description = "에셋 상세", example = "")
        private String assetDetail;
        @Schema(description = "유의사항", example = "")
        private String assetNotice;
        @Schema(description = "상품저작권 안내", example = "")
        private String assetCopyRight;
        @Schema(description = "웹사이트 방문", example = "")
        private String website;

        @Schema(description = "제작 프로그램", example = "")
        private List<String> productionProgram;
        @Schema(description = "호환 프로그램", example = "")
        private String compatibleProgram;


        @Schema(description = "추천 태그 등록", example = "[\"리깅\", \"3D 모델링\"]")
        private List<String> tagList;
    }

    @Data
    public static class PortfolioCreate {

        @Schema(description = "제목")
        private String title;
        @Schema(description = "시작날")
        private LocalDateTime beginAt;
        @Schema(description = "마감날(진행중이면 null로)")
        private LocalDateTime endAt;
        @Schema(description = "작업 인원수")
        private int workerCnt;
        @Schema(description = "작업툴")
        private String tool;
        @Schema(description = "첨부 파일")
        private String referenceFile;
    }

    @Data
    public static class PortfolioUpdate {

        @Schema(description = "제목")
        private String title;
        @Schema(description = "시작날")
        private LocalDateTime beginAt;
        @Schema(description = "마감날(진행중이면 null로)")
        private LocalDateTime endAt;
        @Schema(description = "작업 인원수")
        private int workerCnt;
        @Schema(description = "작업툴")
        private String tool;
        @Schema(description = "첨부 파일")
        private String referenceFile;
    }

}
