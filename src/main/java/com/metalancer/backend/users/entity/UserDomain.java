package com.metalancer.backend.users.entity;

import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import lombok.Builder;
import lombok.Getter;


@Getter
public class UserDomain {

    private Long userId;
    private String email;
    private String nickname;
    private String mobile;
    private String profileImg;
    private Role role;
    private LoginType loginType;

    @Builder
    public UserDomain(Long userId, String email, String nickname, String mobile, String profileImg,
        Role role, LoginType loginType) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.mobile = mobile;
        this.profileImg = profileImg;
        this.role = role;
        this.loginType = loginType;
    }
}
