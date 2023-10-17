package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.BaseEntity;
import com.metalancer.backend.common.constants.DataStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "approve_link")
@ToString
public class ApproveLink extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "approve_link_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String email;
    private String approveLink;
    private boolean isApproved = false;
    private LocalDateTime approvedAt;

    @Builder
    public ApproveLink(User user, String email, String approveLink) {
        this.user = user;
        this.email = email;
        this.approveLink = approveLink;
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
