package com.metalancer.backend.users.domain;

import lombok.Builder;
import lombok.Getter;


@Getter
public class Creator {

    private Long creatorId;
    private String profileImg;
    private String nickName;
    private String email;
    private String introduction;
    private String introductionShort;
    private int taskCnt;
    private double satisficationRate;
    private String userType;

    @Builder
    public Creator(Long creatorId, String profileImg, String nickName, String email,
        String introduction, String introductionShort, int cnt, double satisficationRate,
        String userType) {
        this.creatorId = creatorId;
        this.profileImg = profileImg;
        this.nickName = nickName;
        this.email = email;
        this.introduction = introduction;
        this.introductionShort = introductionShort;
        this.taskCnt = cnt;
        this.satisficationRate = satisficationRate;
        this.userType = userType;
    }
}
