package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum GenreGalaxyType {

    MODEL("모델"),
    ANIMAL("동물"),
    PLANT("식물"),
    BACKGROUND("배경"),
    FOOD("음식"),
    OBJECTS("사물"),
    ROBOT("로봇"),
    VFX("VFX"),
    ETC("기타");
    
    private final String korName;

    private GenreGalaxyType(String korName) {
        this.korName = korName;
    }
}
