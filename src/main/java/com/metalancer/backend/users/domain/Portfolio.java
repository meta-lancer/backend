package com.metalancer.backend.users.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Portfolio {

    private Long portfolioId;
    private String title;
    private LocalDateTime beginAt;
    private LocalDateTime endAt;
    private int workerCnt;
    private String tool;
    private String referenceFile;

    @Builder
    public Portfolio(Long portfolioId, String title, LocalDateTime beginAt, LocalDateTime endAt,
        int workerCnt, String tool, String referenceFile) {
        this.portfolioId = portfolioId;
        this.title = title;
        this.beginAt = beginAt;
        this.endAt = endAt;
        this.workerCnt = workerCnt;
        this.tool = tool;
        this.referenceFile = referenceFile;
    }
}
