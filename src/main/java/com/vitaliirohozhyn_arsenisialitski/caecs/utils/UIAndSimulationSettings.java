package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

public class UIAndSimulationSettings {
    public Boolean physicsEnabled;
    public ToolBarInstrument selectedInstrument;
    public Float timeBetweenIterations; // per second

    public UIAndSimulationSettings(boolean a_physEnabled, ToolBarInstrument a_selectedInstrument,
            float a_timeBetweenIterations) {
        this.physicsEnabled = a_physEnabled;
        this.selectedInstrument = a_selectedInstrument;
        this.timeBetweenIterations = a_timeBetweenIterations;
    }
}
