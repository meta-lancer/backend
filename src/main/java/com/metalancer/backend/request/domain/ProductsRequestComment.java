package com.metalancer.backend.request.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductsRequestComment {

    private final Long productsRequestCommentId;
    private final ProductsRequest productsRequest;
    private final String nickname;
    private final String profileImg;
    private final String content;
    private final String createdAtKor;
    private final String updatedAtKor;
    private final String createdAtEng;
    private final String updatedAtEng;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private boolean isWriter;

    @Builder
    public ProductsRequestComment(Long productsRequestCommentId, ProductsRequest productsRequest,
        String nickname, String profileImg, String content, String createdAtKor,
        String updatedAtKor, String createdAtEng, String updatedAtEng, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.productsRequestCommentId = productsRequestCommentId;
        this.productsRequest = productsRequest;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.content = content;
        this.createdAtKor = createdAtKor;
        this.updatedAtKor = updatedAtKor;
        this.createdAtEng = createdAtEng;
        this.updatedAtEng = updatedAtEng;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void checkIsWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }
}
