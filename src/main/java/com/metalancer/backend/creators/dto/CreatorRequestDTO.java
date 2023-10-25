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
        @Schema(description = "에셋 상세", example = "")
        private String assetDetail;
        @Schema(description = "유의사항", example = "")
        private String assetNotice;
        @Schema(description = "상품저작권 안내", example = "")
        private String assetCopyRight;

        @Schema(description = "제작 프로그램", example = "Cinema 4D")
        private List<String> productionProgram;
        @Schema(description = "호환 프로그램", example = "C4D, 블렌더, 유니티")
        private List<String> compatibleProgram;
        @Schema(description = "파일크기(MB 기준)", example = "973.4")
        private Double fileSize;
        @Schema(description = "애니메이션", example = "")
        private Boolean animation;
        @Schema(description = "리깅", example = "")
        private Boolean rigging;
        @Schema(description = "확장", example = "fbx, stl, obj, c4d, 3ds")
        private List<String> extList;
        @Schema(description = "지원", example = "웹사이트 방문")
        private String support;
        @Schema(description = "저작권", example = "비독점적 사용권 상업적/비상업적 사용가능")
        private String copyRight;
        @Schema(description = "최신 버전", example = "1.12")
        private String recentVersion;

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
