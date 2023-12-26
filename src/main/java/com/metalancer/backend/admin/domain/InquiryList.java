package com.metalancer.backend.admin.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class InquiryList {

    private Long inquiryId;
    private Long memberId;
    private String profileImg;
    private String nickname;
    private String title;
    private String content;
    private String createdDate;
    private String createdAt;
    private String updatedAt;

    @Builder
    public InquiryList(Long inquiryId, Long memberId, String profileImg, String nickname,
        String title,
        String content, String createdDate, String createdAt, String updatedAt) {
        this.inquiryId = inquiryId;
        this.memberId = memberId;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}