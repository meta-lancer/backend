package com.metalancer.backend.users.dto;

import com.metalancer.backend.common.constants.ValidationText;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserRequestDTO {

    @Data
    @NoArgsConstructor
    public static class CreateRequest {

        @Email(regexp = "", message = ValidationText.EMAIL_INVALID_REGEX)
        private String email;
        @Size(min = 8, max = 30, message = ValidationText.PASSWORD_LENGTH_VALIDATION)
        private String password;
        @Size(max = 30, message = ValidationText.STRING_SHORT_LENGTH_VALIDATION)
        private String job;
        @Size(min = 3, message = ValidationText.SHOULD_BE_MORE_THAN_3)
        private List<String> interests;
        @AssertTrue(message = ValidationText.TRUE_REQUIRED)
        private boolean ageAgree;
        @AssertTrue(message = ValidationText.TRUE_REQUIRED)
        private boolean serviceAgree;
        @AssertTrue(message = ValidationText.TRUE_REQUIRED)
        private boolean infoAgree;
        private boolean marketingAgree;
        private boolean statusAgree;
        
        public String setPasswordEncrypted(PasswordEncoder passwordEncoder) {
            return passwordEncoder.encode(password);
        }
    }

}
