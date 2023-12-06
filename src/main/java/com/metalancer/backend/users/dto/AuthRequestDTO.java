package com.metalancer.backend.users.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthRequestDTO {

    @Data
    @NoArgsConstructor
    public static class LoginRequest {

        private String email;
        private String password;
    }

    @Data
    @NoArgsConstructor
    public static class PasswordRequest {

        private String originalPassword;
        private String newPassword1;
        private String newPassword2;

        public boolean newPasswordEquals() {
            return newPassword1.equals(newPassword2);
        }
    }

}
