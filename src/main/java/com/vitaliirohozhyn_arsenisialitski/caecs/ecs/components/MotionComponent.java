package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;

import java.awt.geom.Point2D;

class MotionComponent implements Component {
    public Point2D.Float velocity;
    public Point2D.Float acceleration;

    public MotionComponent(Point2D.Float a_velocity, Point2D.Float a_accelelration) {
        this.velocity = a_velocity;
        this.acceleration = a_accelelration;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Velocity:");
        build.append(this.velocity.toString());
        build.append("\nAcceleration:");
        build.append(this.acceleration.toString());
        return build.toString();
    }
}
