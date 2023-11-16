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
        String introduction, String introductionShort,
        String userType) {
        this.creatorId = creatorId;
        this.profileImg = profileImg;
        this.nickName = nickName;
        this.email = email;
        this.introduction = introduction;
        this.introductionShort = introductionShort;
        this.userType = userType;
    }

    public void setTaskCnt(long taskCnt) {
        this.taskCnt = (int) taskCnt;
    }

    public void setSatisficationRate(double satisficationRate) {
        this.satisficationRate = satisficationRate;
    }
}
