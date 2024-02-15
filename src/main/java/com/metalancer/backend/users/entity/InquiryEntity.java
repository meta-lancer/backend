package com.metalancer.backend.users.entity;

import com.metalancer.backend.admin.domain.InquiryList;
import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.utils.Time;
import com.metalancer.backend.users.domain.MyInquiryList;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "inquiry")
@ToString
public class InquiryEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7484129177241993156L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "inquiry_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private Long orderProductId;

    private Long adminId;

    private String adminName;

    private boolean reply = false;

    private String replyContent;

    private LocalDateTime replyAt;

    private LocalDateTime replyUpdatedAt;
    private String fileUrl;

    @Builder
    public InquiryEntity(User user, String title, String content, Long orderProductId) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.orderProductId = orderProductId;
    }

    public void reply(Long adminId, String adminName, String replyContent) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.replyContent = replyContent;
        this.reply = true;
        if (replyAt == null) {
            this.replyAt = LocalDateTime.now();
        } else {
            this.replyUpdatedAt = LocalDateTime.now();
        }
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }


    public InquiryList toInquiryList() {
        return InquiryList.builder().inquiryId(id).memberId(user.getId())
            .profileImg(user.getProfileImg())
            .nickname(user.getNickname()).title(title)
            .content(content).createdDate(Time.convertDateToStringWithDot(getCreatedAt()))
            .createdAt(Time.convertDateToKor(getCreatedAt()))
            .updatedAt(Time.convertDateToKor(getUpdatedAt()))
            .adminId(adminId).adminName(adminName).reply(reply).replyContent(replyContent)
            .replyAt(replyAt).replyUpdatedAt(replyUpdatedAt)
            .fileUrl(fileUrl)
            .build();
    }

    public MyInquiryList toMyInquiryList() {
        return MyInquiryList.builder().inquiryId(id)
            .title(title)
            .content(content).createdDate(Time.convertDateToStringWithDot(getCreatedAt()))
            .createdAt(Time.convertDateToKor(getCreatedAt()))
            .updatedAt(Time.convertDateToKor(getUpdatedAt()))
            .adminName(adminName).reply(reply).replyContent(replyContent)
            .replyAt(replyAt).replyUpdatedAt(replyUpdatedAt)
            .fileUrl(fileUrl)
            .build();
    }
}
