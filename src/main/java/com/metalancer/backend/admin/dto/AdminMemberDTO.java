package com.metalancer.backend.admin.dto;

import com.metalancer.backend.common.constants.DataStatus;
import com.metalancer.backend.common.constants.Role;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AdminMemberDTO {

    @Data
    @NoArgsConstructor
    public static class Approve {

        private List<Long> userIdList;
    }

    @Data
    @NoArgsConstructor
    public static class UpdateUser {

        private Long memberId;
        private String name;
        private String username;
        private String mobile;
        private String job;
        private Role role;
        private DataStatus status;
    }

}
