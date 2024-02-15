package com.metalancer.backend.users.domain;

import com.metalancer.backend.common.utils.Time;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyInquiryList {

    private Long inquiryId;
    private String title;
    private String content;
    private String fileUrl;
    private String createdDate;
    private String createdAt;
    private String updatedAt;

    private String adminName;
    private boolean reply;
    private String replyContent;
    private String replyAt;
    private String replyUpdatedAt;

    @Builder
    public MyInquiryList(Long inquiryId, String title,
        String content, String fileUrl, String createdDate, String createdAt, String updatedAt,
        String adminName, boolean reply, String replyContent, LocalDateTime replyAt,
        LocalDateTime replyUpdatedAt) {
        this.inquiryId = inquiryId;
        this.fileUrl = fileUrl;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.adminName = adminName;
        this.reply = reply;
        this.replyContent = replyContent;
        this.replyAt = Time.convertDateToFullString(replyAt);
        this.replyUpdatedAt = Time.convertDateToFullString(replyUpdatedAt);
    }
}