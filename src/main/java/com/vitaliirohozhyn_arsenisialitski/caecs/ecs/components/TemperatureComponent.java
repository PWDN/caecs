package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;

/**
 * Komponent, przechowywujący temperaturę {@link Entity} oraz czy jest na ogniu.
 */
public class TemperatureComponent implements Component {
    public Double temperature; // In Kelvins
    public Boolean isOnFire;

    public TemperatureComponent(double a_temperature, boolean a_isOnFire) {
        this.temperature = a_temperature;
        this.isOnFire = a_isOnFire;
    }

    public TemperatureComponent(JSONObject a_obj) throws JSONException {
        this.temperature = a_obj.getDouble("temperature");
        this.isOnFire = a_obj.getBoolean("isOnFire");
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Temperature(in Kelvins):");
        build.append(this.temperature);
        build.append("\nIs on fire:");
        build.append(this.isOnFire);
        return build.toString();
    }

    public JSONObject toJSON() {
        return new JSONObject().put("temperatureComponent",
                new JSONObject().put("temperature", this.temperature).put("isOnFire", this.isOnFire));
    }
}
