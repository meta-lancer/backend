package com.metalancer.backend.user.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.DataStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "approve_link")
@ToString
public class ApproveLink extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "approve_link_id", nullable = false)
    private Long id;
    private String email;
    private String approveLink;
    private boolean isApproved = false;
    private LocalDateTime approvedAt;

    @Builder
    public ApproveLink(String email, String approveLink) {
        this.email = email;
        this.approveLink = approveLink;
    }

    public DataStatus getStatus() {
        return getStatusvalue();
    }

    public void isUserActive() {
        DataStatus status = getStatus();
//        String USER_STATUS_ERROR = "user status error";
//        switch (status) {
//            case DELETED -> throw new StatusException(USER_STATUS_ERROR, ErrorCode.STATUS_DELETED);
//            case PENDING -> throw new StatusException(USER_STATUS_ERROR, ErrorCode.STATUS_PENDING);
//            case BANNED -> throw new StatusException(USER_STATUS_ERROR, ErrorCode.STATUS_BANNED);
//        }
    }

    public void approve() {
        this.isApproved = true;
        this.approvedAt = LocalDateTime.now();
    }
}
