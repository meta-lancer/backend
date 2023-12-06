package com.metalancer.backend.admin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.metalancer.backend.users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDetail {

    private User user;
    @JsonProperty("isCreator")
    private boolean isCreator;

    @Builder
    public MemberDetail(User user, boolean isCreator) {
        this.user = user;
        this.isCreator = isCreator;
    }
}