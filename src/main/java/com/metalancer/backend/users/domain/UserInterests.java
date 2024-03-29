package com.metalancer.backend.users.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public class UserInterests {
    
    private String interestsName;
    private int ord;

    @Builder
    public UserInterests(String interestsName, int ord) {
        this.interestsName = interestsName;
        this.ord = ord;
    }
}

