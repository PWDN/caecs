package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;

public class ChargeComponent implements Component {
    public Double charge; // in Culombs(subject to change)

    public ChargeComponent(double a_charge) {
        this.charge = a_charge;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Charge(in Culombs):");
        build.append(this.charge);
        return build.toString();
    }
}
