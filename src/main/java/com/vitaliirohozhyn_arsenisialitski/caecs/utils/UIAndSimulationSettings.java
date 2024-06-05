package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

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
}
