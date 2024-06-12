package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import org.json.JSONObject;

/**
 * Interfejs, opisujący komponent i wymagane od niego metody.
 */
public interface Component {
    /**
     * Metoda do generacji {@link String} na podstawie danych obiektu kompontentu.
     * Przydatne do debug'u
     *
     * @return zgenerowany {@link String}
     */
    public String toString();

    /**
     * Metoda do serializacji danego komponentu
     *
     * @return zgenerowany {@link JSONObject}
     */
    public JSONObject toJSON();
}
