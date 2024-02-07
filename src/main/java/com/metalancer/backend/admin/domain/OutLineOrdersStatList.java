package com.metalancer.backend.admin.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OutLineOrdersStatList {

    private Long tagsId;
    private String tagName;
    private String tagNameEn;
    private Integer cnt;

    @Builder
    public OutLineOrdersStatList(Long tagsId, String tagName, String tagNameEn) {
        this.tagsId = tagsId;
        this.tagName = tagName;
        this.tagNameEn = tagNameEn;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}