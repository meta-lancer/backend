package com.metalancer.backend.admin.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRegisterDailyStat {

    private String date;
    private Integer registerCnt;
    private Integer creatorRegisterCnt;
    private Integer loginCnt;

    @Builder
    public MemberRegisterDailyStat(String date, Integer registerCnt, Integer creatorRegisterCnt,
        Integer loginCnt) {
        this.date = date;
        this.registerCnt = registerCnt;
        this.creatorRegisterCnt = creatorRegisterCnt;
        this.loginCnt = loginCnt;

    }
}