package com.metalancer.backend.admin.domain;

import java.time.LocalDateTime;
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

    private Long adminId;
    private String adminName;
    private boolean reply;
    private String replyContent;
    private LocalDateTime replyAt;
    private LocalDateTime replyUpdatedAt;

    @Builder
    public InquiryList(Long inquiryId, Long memberId, String profileImg, String nickname,
        String title,
        String content, String createdDate, String createdAt, String updatedAt, Long adminId,
        String adminName, boolean reply, String replyContent, LocalDateTime replyAt,
        LocalDateTime replyUpdatedAt) {
        this.inquiryId = inquiryId;
        this.memberId = memberId;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.adminId = adminId;
        this.adminName = adminName;
        this.reply = reply;
        this.replyContent = replyContent;
        this.replyAt = replyAt;
        this.replyUpdatedAt = replyUpdatedAt;
    }
}