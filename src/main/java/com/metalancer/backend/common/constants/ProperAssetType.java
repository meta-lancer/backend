package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum ProperAssetType {

    PRINTING("3D 프린팅"),
    RIGGING("리깅"),
    TEXTURE("텍스쳐"),
    ARCHITECTURE("건축");

    private final String korName;

    private ProperAssetType(String korName) {
        this.korName = korName;
    }
}
