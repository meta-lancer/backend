package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterList {

    private Long memberId;
    private String email;
    private String name;
    private String username;
    private String mobile;
    private String loginType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DataStatus status;
    private String approveLink;
    private String receivedEmail;
    private boolean approveStatus;

    @Builder
    public RegisterList(Long memberId, String email, String name, String username, String mobile,
        LoginType loginType, LocalDateTime createdAt,
        LocalDateTime updatedAt,
        DataStatus status) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.loginType = loginType.getProvider();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public void setApproveInfo(String approveLink, String receivedEmail, boolean approveStatus) {
        this.approveLink = approveLink;
        this.receivedEmail = receivedEmail;
        this.approveStatus = approveStatus;
    }
}