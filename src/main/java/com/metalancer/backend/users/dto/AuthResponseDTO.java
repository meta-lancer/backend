package com.metalancer.backend.users.dto;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.LoginType;
import com.metalancer.backend.common.constants.Role;
import com.metalancer.backend.users.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthResponseDTO {

    @Data
    @NoArgsConstructor
    public static class userInfo {

        private Long userId;
        private String email;
        private String oauthId;
        private String name;
        private String username;
        private String mobile;
        private LoginType loginType;
        private DataStatus status;
        private Role role;

        public userInfo(User user) {
            this.userId = user.getId();
            this.email = user.getEmail();
            this.oauthId = user.getOauthId();
            this.name = user.getName();
            this.username = user.getUsername();
            this.mobile = user.getMobile();
            this.loginType = user.getLoginType();
            this.status = user.getStatus();
            this.role = user.getRole();
        }
    }

}
