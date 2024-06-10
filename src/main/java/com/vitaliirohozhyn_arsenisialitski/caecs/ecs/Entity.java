package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ChargeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ColorComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialStateComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.TemperatureComponent;

public class Entity {
    private final ArrayList<Component> componentList;

    public Entity() {
        this.componentList = new ArrayList<Component>();
    }

    public Entity(Component[] a_componentList) {
        this.componentList = new ArrayList<Component>();
        for (Component i : a_componentList) {
            this.addComponent(i);
        }
    }

    public Entity(JSONArray a_componentListJSON) throws JSONException {
        this.componentList = new ArrayList<Component>();
        for (Object i : a_componentListJSON) {
            JSONObject comp = (JSONObject) i;
            String key = comp.keySet().toArray()[0].toString();
            switch (key) {
                case "chargeComponent":
                    this.addComponent(new ChargeComponent(comp.getJSONObject(key)));
                    break;
                case "colorComponent":
                    this.addComponent(new ColorComponent(comp.getJSONObject(key)));
                    break;
                case "materialStateComponent":
                    this.addComponent(new MaterialStateComponent(comp.getJSONObject(key)));
                    break;
                case "materialTypeComponent":
                    this.addComponent(new MaterialTypeComponent(comp.getJSONObject(key)));
                    break;
                case "positionComponent":
                    this.addComponent(new PositionComponent(comp.getJSONObject(key)));
                    break;
                case "temperatureComponent":
                    this.addComponent(new TemperatureComponent(comp.getJSONObject(key)));
                    break;
                default:
                    throw new JSONException("Unexpected component found: " + key);
            }
        }
    }

    public void addComponent(Component a_component) {
        this.componentList.add(a_component);
    }

    public boolean doesEntityHasComponentOfType(Class<? extends Component> a_class) {
        for (Component i : this.componentList) {
            if (a_class == i.getClass())
                return true;
        }
        return false;
    }

    public <T extends Component> T getFirstComponentOfType(Class<T> a_class) {
        for (Component i : this.componentList) {
            if (a_class == i.getClass())
                return (T) i;
        }
        throw new RuntimeException("Unhandled search of component in entity");
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        for (Component i : this.componentList) {
            build.append(i.toString());
            if (i != this.componentList.get(this.componentList.size() - 1))
                build.append("\n");
        }
        return build.toString();
    }

    public JSONArray toJSON() {
        JSONArray res = new JSONArray();
        for (Component i : this.componentList) {
            res.put(i.toJSON());
        }
        return res;
    };
}
