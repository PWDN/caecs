package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;

public class PositionComponent implements Component {
    public Integer x, y;

    public PositionComponent(int a_x, int a_y) {
        this.x = a_x;
        this.y = a_y;
    }

    public PositionComponent(JSONObject a_obj) throws JSONException {
        this.x = a_obj.getInt("x");
        this.y = a_obj.getInt("y");
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Position X:");
        build.append(this.x);
        build.append("\nPosition Y:");
        build.append(this.y);
        return build.toString();
    }

    public JSONObject toJSON() {
        return new JSONObject().put("positionComponent",
                new JSONObject().put("x", this.x).put("y", this.y));
    }
}
