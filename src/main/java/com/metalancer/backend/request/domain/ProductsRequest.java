package com.metalancer.backend.request.domain;

import com.metalancer.backend.category.dto.CategoryDTO.RequestCategory;
import com.metalancer.backend.common.constants.ProductsRequestStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductsRequest {

    private final Long productsRequestId;
    private final Long writerId;
    private final String nickname;
    private final String profileImg;
    private List<RequestCategory> productsRequestTypeList;
    private final ProductsRequestStatus productsRequestStatus;
    private final String title;
    private final String content;
    private final String createdAtKor;
    private final String updatedAtKor;
    private final String createdAtEng;
    private final String updatedAtEng;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private Integer commentCnt;

    @Builder
    public ProductsRequest(Long productsRequestId, Long writerId, String nickname,
        String profileImg,
        ProductsRequestStatus productsRequestStatus, String title, String content,
        String createdAtKor, String updatedAtKor,
        String createdAtEng, String updatedAtEng, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.productsRequestId = productsRequestId;
        this.writerId = writerId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.productsRequestStatus = productsRequestStatus;
        this.title = title;
        this.content = content;
        this.createdAtKor = createdAtKor;
        this.updatedAtKor = updatedAtKor;
        this.createdAtEng = createdAtEng;
        this.updatedAtEng = updatedAtEng;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setProductionRequestTypeList(
        List<RequestCategory> requestCategoryList) {
        this.productsRequestTypeList = requestCategoryList;
    }

    public void setCommentCnt(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }
}
