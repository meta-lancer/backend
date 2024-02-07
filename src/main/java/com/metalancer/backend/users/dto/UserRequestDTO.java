package com.metalancer.backend.users.dto;

import com.metalancer.backend.common.constants.ValidationText;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@NoArgsConstructor
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

    @Data
    @NoArgsConstructor
    public static class UpdateCareerIntroRequest {

        private String intro;
    }

    @Data
    @NoArgsConstructor
    public static class CreateCareerRequest {

        private String title;
        private LocalDateTime beginAt;
        private LocalDateTime endAt;
        private String description;
    }

    @Data
    @NoArgsConstructor
    public static class UpdateCareerRequest {

        private String title;
        private LocalDateTime beginAt;
        private LocalDateTime endAt;
        private String description;
    }

    @Data
    @NoArgsConstructor
    public static class UpdateBasicInfo {

        private String profileImg;
        private String nickname;
        private String introduction;
        private String link;
        private String job;
        private List<String> interestsList;
    }

    @Data
    @NoArgsConstructor
    public static class CreateOauthRequest {

        @Schema(description = "유저-true, 크리에이터-false")
        private boolean isNormalUser;
        @Email(regexp = "", message = ValidationText.EMAIL_INVALID_REGEX)
        private String email;
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
    }

    @Data
    @NoArgsConstructor
    public static class CreateInquiryRequest {

        private String title;
        private String content;
//        private Long orderProductId;
    }

    @Data
    @NoArgsConstructor
    public static class CreateCartRequest {

        private Long assetId;
        private Long requestOptionId;

    }

    @Data
    @NoArgsConstructor
    public static class DeleteCartRequest {

        private Long assetId;
        private Long requestOptionId;

    }
}
