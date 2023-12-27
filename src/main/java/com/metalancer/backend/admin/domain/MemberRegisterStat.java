package com.metalancer.backend.admin.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRegisterStat {

    private Integer totalUserCnt;
    private Integer normalUserCnt;
    private Integer creatorUserCnt;
    private Integer withdrewCnt;
    private Integer emailRegisterCnt;
    private Integer googleRegisterCnt;
    private Integer naverRegisterCnt;
    private Integer kakaoRegisterCnt;

    @Builder
    public MemberRegisterStat(Integer totalUserCnt, Integer normalUserCnt, Integer creatorUserCnt,
        Integer withdrewCnt, Integer emailRegisterCnt, Integer googleRegisterCnt,
        Integer naverRegisterCnt, Integer kakaoRegisterCnt) {
        this.totalUserCnt = totalUserCnt;
        this.normalUserCnt = normalUserCnt;
        this.creatorUserCnt = creatorUserCnt;
        this.withdrewCnt = withdrewCnt;
        this.emailRegisterCnt = emailRegisterCnt;
        this.googleRegisterCnt = googleRegisterCnt;
        this.naverRegisterCnt = naverRegisterCnt;
        this.kakaoRegisterCnt = kakaoRegisterCnt;
    }
}