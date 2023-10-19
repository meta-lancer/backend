package com.metalancer.backend.users.dto;

import com.metalancer.backend.interests.domain.Interests;
import com.metalancer.backend.users.domain.Career;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class UserResponseDTO {

    @Data
    public static class BasicInfo {

        private String profileImg;
        private String nickname;
        private String email;
        private String introduction;
        private String link;
        private String job;
        private List<Interests> interestsList;

        @Builder
        public BasicInfo(String profileImg, String nickname, String email, String introduction,
            String link,
            String job, List<Interests> interestsList) {
            this.profileImg = profileImg;
            this.nickname = nickname;
            this.email = email;
            this.introduction = introduction;
            this.link = link;
            this.job = job;
            this.interestsList = interestsList;
        }
    }

    @Data
    public static class IntroAndExperience {

        private String introduction;
        private List<Career> careerList;

        @Builder
        public IntroAndExperience(String introduction, List<Career> careerList) {
            this.introduction = introduction;
            this.careerList = careerList;
        }
    }
}
