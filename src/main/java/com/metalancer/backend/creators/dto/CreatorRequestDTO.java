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
        @Schema(description = "주문서 만들기로부터 받은 orderNo", example = "20235325346344")
        private String productsCategory;

        private String compatibleProgram;

        @Schema(description = "모델 타입", example = "")
        private String modelType;


        private String assetDetail;
        private String assetNotice;
        private String assetCopyRight;
        private String website;

        @Schema(description = "제작 프로그램", example = "")
        private List<String> productionProgram;


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
