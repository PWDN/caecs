package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialType;

/**
 * Komponent, przechowywujący typ materiału {@link Entity}. Typy materiałów są
 * zdefiniowane w {@link MaterialType}
 */
public class MaterialTypeComponent implements Component {
    public MaterialType materialType;

    public MaterialTypeComponent(MaterialType a_materialType) {
        this.materialType = a_materialType;
    }

    public MaterialTypeComponent(JSONObject a_obj) throws JSONException {
        this.materialType = MaterialType.values()[a_obj.getInt("materialType")];
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Material type:");
        build.append(this.materialType.name);
        return build.toString();
    }

    public boolean IsConductivityZero() {
        return (this.materialType.conductivity == 0);
    }

    public boolean IsThermConductivityZero() {
        return (this.materialType.thermalConductivity == 0);
    }

    public JSONObject toJSON() {
        return new JSONObject().put("materialTypeComponent",
                new JSONObject().put("materialType", this.materialType.ordinal()));
    }
}
