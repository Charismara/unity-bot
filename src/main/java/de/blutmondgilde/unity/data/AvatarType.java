package de.blutmondgilde.unity.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AvatarType {
    JPEG("jpg"), PNG("png"), WebP("webp"), GIF("gif");

    @Getter
    private final String type;
}
