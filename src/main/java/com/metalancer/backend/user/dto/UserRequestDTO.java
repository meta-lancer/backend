package com.metalancer.backend.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserRequestDTO {

    @Data
    @NoArgsConstructor
    public static class CreateRequest {
        private String email;
        private String password;

        public String setPasswordEncrypted(PasswordEncoder passwordEncoder) {
            return passwordEncoder.encode(password);
        }
    }

}
