package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminManager {

    private Long adminMemberId;
    private String adminName;
    private String adminNickname;
    private String adminMobile;
    private String adminEmail;
    private Role adminRole;
    private LoginType adminLoginType;
    private LocalDateTime adminCreatedAt;
    private LocalDateTime adminUpdatedAt;
    private DataStatus adminStatus;

    @Builder
    public AdminManager(Long memberId, String name, String nickname, String mobile, String email,
        Role role, LoginType loginType, LocalDateTime createdAt, LocalDateTime updatedAt,
        DataStatus status) {
        this.adminMemberId = memberId;
        this.adminName = name;
        this.adminNickname = nickname;
        this.adminMobile = mobile;
        this.adminEmail = email;
        this.adminRole = role;
        this.adminLoginType = loginType;
        this.adminCreatedAt = createdAt;
        this.adminUpdatedAt = updatedAt;
        this.adminStatus = status;
    }
}