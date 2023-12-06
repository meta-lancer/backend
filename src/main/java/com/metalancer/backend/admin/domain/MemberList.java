package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberList {

    private Long memberId;
    private String email;
    private String name;
    private String username;
    private String mobile;
    private String job;
    private Role role;
    private String loginType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DataStatus status;

    @Builder
    public MemberList(Long memberId, String email, String name, String username, String mobile,
        String job, Role role, LoginType loginType, LocalDateTime createdAt,
        LocalDateTime updatedAt,
        DataStatus status) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.username = username;
        this.mobile = mobile;
        this.job = job;
        this.role = role;
        this.loginType = loginType.getProvider();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
}