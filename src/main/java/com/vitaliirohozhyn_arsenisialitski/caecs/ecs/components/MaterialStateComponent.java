package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialState;

import org.json.JSONException;
import org.json.JSONObject;

public class MaterialStateComponent implements Component {
    public MaterialState materialState;

    public MaterialStateComponent(MaterialState a_state) {
        this.materialState = a_state;
    }

    public MaterialStateComponent(JSONObject a_obj) throws JSONException {
        this.materialState = MaterialState.values()[a_obj.getInt("materialState")];
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Material state:");
        build.append(this.materialState);
        return build.toString();
    }

    public JSONObject toJSON() {
        return new JSONObject().put("materialStateComponent",
                new JSONObject().put("materialState", this.materialState.ordinal()));
    }
}
