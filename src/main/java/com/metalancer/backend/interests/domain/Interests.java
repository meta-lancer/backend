package com.metalancer.backend.interests.domain;

import lombok.Builder;
import lombok.Getter;


@Getter
public class Interests {

    private final Long interestsId;
    private final String interestsName;
    private final int ord;

    @Builder
    public Interests(Long interestsId, String interestsName, int ord) {
        this.interestsId = interestsId;
        this.interestsName = interestsName;
        this.ord = ord;
    }
}

