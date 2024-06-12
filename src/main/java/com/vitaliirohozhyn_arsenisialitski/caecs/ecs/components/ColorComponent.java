package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import java.awt.Color;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Komponent przechowywujący obecny kolor {@link Entity}
 */
public class ColorComponent implements Component {
    public Color color;

    public ColorComponent(Color a_color) {
        this.color = a_color;
    }

    public ColorComponent(JSONObject a_obj) throws JSONException {
        this.color = new Color(a_obj.getInt("color"));
    }

    public String toString() {
        return this.color.toString();
    }

    public JSONObject toJSON() {
        return new JSONObject().put("colorComponent", new JSONObject().put("color", this.color.getRGB()));
    }
}
