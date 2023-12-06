package com.metalancer.backend.users.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Career {

    private final Long careerId;
    private final String title;
    private final LocalDateTime beginAt;
    private final LocalDateTime endAt;
    private final String description;

    @Builder
    public Career(Long careerId, String title, LocalDateTime beginAt, LocalDateTime endAt,
        String description) {
        this.careerId = careerId;
        this.title = title;
        this.beginAt = beginAt;
        this.endAt = endAt;
        this.description = description;
    }
}
