package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitaliirohozhyn_arsenisialitski.caecs.graphics.RenderMode;

/**
 * Klasa odpowiadająca za definiowanie ustawień symulacji
 */
public class UIAndSimulationSettings {
    public Boolean gravityEnabled;
    public ToolBarInstrument selectedInstrument;
    public RenderMode renderMode;

    /**
     * Konstruktor klasy
     *
     * @param a_gravEnabled        czy domyślnie włączyć symulację grawitacji
     * @param a_selectedInstrument jaki jest wybrany domyślnie instrument
     * @param a_renderMode         sposób renderu
     */
    public UIAndSimulationSettings(boolean a_gravEnabled, ToolBarInstrument a_selectedInstrument,
            RenderMode a_renderMode) {
        this.gravityEnabled = a_gravEnabled;
        this.selectedInstrument = a_selectedInstrument;
        this.renderMode = a_renderMode;
    }

    public JSONObject toJSON() {
        return new JSONObject().put("gravityEnabled", this.gravityEnabled)
                .put("selectedInstrument", this.selectedInstrument.ordinal())
                .put("renderMode", this.renderMode.ordinal());
    }

    public void fromJSON(JSONObject settings) throws JSONException {
        for (String key : settings.keySet()) {
            switch (key) {
                case "gravityEnabled":
                    this.gravityEnabled = settings.getBoolean(key);
                    break;
                case "selectedInstrument":
                    this.selectedInstrument = ToolBarInstrument.values()[settings.getInt(key)];
                    break;
                case "renderMode":
                    this.renderMode = RenderMode.values()[settings.getInt(key)];
                    break;
                default:
                    throw new JSONException("Unexpected settings key");
            }
        }
    }
}
