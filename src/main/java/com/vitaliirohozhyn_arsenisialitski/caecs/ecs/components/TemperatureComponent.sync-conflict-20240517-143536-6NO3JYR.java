package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;

class TemperatureComponent implements Component {
    public Integer temperature; // In Kelvins
    public Boolean isOnFire;

    public TemperatureComponent(int a_temperature, boolean a_isOnFire) {
        this.temperature = a_temperature;
        this.isOnFire = a_isOnFire;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Temperature(in Kelvins):");
        build.append(this.temperature);
        build.append("\nIs on fire:");
        build.append(this.isOnFire);
        return build.toString();
    }
}
