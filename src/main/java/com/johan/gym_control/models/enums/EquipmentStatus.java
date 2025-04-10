package com.johan.gym_control.models.enums;

public enum EquipmentStatus {
    AVAILABLE("disponible"),
    LOANED("prestado"),
    UNAVAILABLE("no disponible");

    private final String displayName;

    EquipmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
