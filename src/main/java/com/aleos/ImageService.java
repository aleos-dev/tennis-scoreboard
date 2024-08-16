package com.aleos;

import com.aleos.util.PropertiesUtil;
import lombok.NonNull;

public class ImageService {

    public static final String DEFAULT_PLAYER_IMAGE_PATH = PropertiesUtil.get("image.default.player.path").orElseThrow();

    private final String basePath = PropertiesUtil.get("image.default.base.path").orElseThrow();


    public String getExternalImagePath() {
        return this.getClass().getResource(DEFAULT_PLAYER_IMAGE_PATH).toExternalForm();
    }

    public String resolveImageUrlForCountry(@NonNull String country) {
        return null;
    }
}
