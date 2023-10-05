package com.metalancer.backend.request.domain;

import com.metalancer.backend.common.constants.ProductionRequestType;
import java.time.LocalDateTime;
import lombok.Builder;

public class ProductsRequest {

    private final Long writerId;
    private final String nickname;
    private final String profileImg;
    private final ProductionRequestType productionRequestType;
    private final String title;
    private final String content;
    private final String createdAtKor;
    private final String updatedAtKor;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private Integer commentCnt;

    @Builder
    public ProductsRequest(Long writerId, String nickname, String profileImg,
        ProductionRequestType productionRequestType, String title, String content,
        String createdAtKor, String updatedAtKor,
        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.writerId = writerId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.productionRequestType = productionRequestType;
        this.title = title;
        this.content = content;
        this.createdAtKor = createdAtKor;
        this.updatedAtKor = updatedAtKor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setCommentCnt(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }
}
