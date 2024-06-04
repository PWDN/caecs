package com.vitaliirohozhyn_arsenisialitski.caecs.graphics;

public enum RenderMode {
    NORMAL("Normal"),
    ELECTRICAL("Electrical"),
    THERMAL("Thermal");

    public final String name;

    private RenderMode(String a_name) {
        this.name = a_name;
    }
}
