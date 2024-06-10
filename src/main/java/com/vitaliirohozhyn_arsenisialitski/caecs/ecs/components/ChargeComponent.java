package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import java.io.Serializable;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;

import org.json.JSONException;
import org.json.JSONObject;

public class ChargeComponent implements Component {
    public Double charge;

    public ChargeComponent(double a_charge) {
        this.charge = a_charge;
    }

    public ChargeComponent(JSONObject a_obj) throws JSONException {
        this.charge = a_obj.getDouble("charge");
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Charge:");
        build.append(this.charge);
        return build.toString();
    }

    public JSONObject toJSON() {
        return new JSONObject().put("chargeComponent", new JSONObject().put("charge", this.charge));
    }
}
