package com.metalancer.backend.admin.domain;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminManager {

    private Long memberId;
    private String name;
    private String nickname;
    private String mobile;
    private String email;
    private Role role;
    private LoginType loginType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DataStatus status;

    @Builder
    public AdminManager(Long memberId, String name, String nickname, String mobile, String email,
        Role role, LoginType loginType, LocalDateTime createdAt, LocalDateTime updatedAt,
        DataStatus status) {
        this.memberId = memberId;
        this.name = name;
        this.nickname = nickname;
        this.mobile = mobile;
        this.email = email;
        this.role = role;
        this.loginType = loginType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
}