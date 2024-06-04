package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

public class UIAndSimulationSettings {
    public Boolean physicsEnabled;
    public ToolBarInstrument selectedInstrument;
    public Float timeBetweenIterations; // per second
    public VisionRender VisionRenders;

    public UIAndSimulationSettings(boolean a_physEnabled, ToolBarInstrument a_selectedInstrument,
            float a_timeBetweenIterations, VisionRender a_visionRender) {
        this.physicsEnabled = a_physEnabled;
        this.selectedInstrument = a_selectedInstrument;
        this.timeBetweenIterations = a_timeBetweenIterations;
        this.VisionRenders = a_visionRender;
    }

    public UIAndSimulationSettings(boolean a_physEnabled, ToolBarInstrument a_selectedInstrument,
    float a_timeBetweenIterations) {
    this.physicsEnabled = a_physEnabled;
    this.selectedInstrument = a_selectedInstrument;
    this.timeBetweenIterations = a_timeBetweenIterations;
    this.VisionRenders = VisionRenders.NORMAL;
}

}
