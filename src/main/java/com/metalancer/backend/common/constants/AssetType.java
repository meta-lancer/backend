package com.metalancer.backend.common.constants;

public enum AssetType {
    THUMBNAIL("thumbnail"),
    ZIP("zip"),
    VIEWS_3D("3dViews");

    private final String path;

    AssetType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
