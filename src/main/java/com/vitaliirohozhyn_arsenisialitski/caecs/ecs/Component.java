package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import org.json.JSONObject;

public interface Component {
    public String toString();

    public JSONObject toJSON();
}
