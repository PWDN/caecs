package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

import com.vitaliirohozhyn_arsenisialitski.caecs.graphics.RenderMode;

public class UIAndSimulationSettings {
    public Boolean physicsEnabled;
    public ToolBarInstrument selectedInstrument;
    public Float timeBetweenIterations; // per second
    public RenderMode renderMode;

    public UIAndSimulationSettings(boolean a_physEnabled, ToolBarInstrument a_selectedInstrument,
            float a_timeBetweenIterations, RenderMode a_renderMode) {
        this.physicsEnabled = a_physEnabled;
        this.selectedInstrument = a_selectedInstrument;
        this.timeBetweenIterations = a_timeBetweenIterations;
        this.renderMode = a_renderMode;
    }
}
