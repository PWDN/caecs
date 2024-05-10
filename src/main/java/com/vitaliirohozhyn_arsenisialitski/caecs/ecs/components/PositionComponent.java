package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;

public class PositionComponent implements Component {
    public Integer x, y;

    public PositionComponent(int a_x, int a_y) {
        this.x = a_x;
        this.y = a_y;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Position X:");
        build.append(this.x);
        build.append("\nPosition Y:");
        build.append(this.y);
        return build.toString();
    }
}
