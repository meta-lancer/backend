package com.metalancer.backend.user.dto;

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

}
