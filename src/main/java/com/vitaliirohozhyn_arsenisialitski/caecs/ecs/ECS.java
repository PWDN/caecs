package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaliirohozhyn_arsenisialitski.caecs.graphics.RenderMode;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.ToolBarInstrument;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.UIAndSimulationSettings;

/**
 * Klasa odpowiadająca za symulację światu
 */
public class ECS {
    private final ArrayList<ECSSystem> systemList;
    private final ExecutorService executor;
    private final HashMap<Entity, ArrayList<Callable<Void>>> entitiesThreadedList;
    public final UIAndSimulationSettings settings;
    private final HashSet<Entity> entityList;

    /**
     * Konstruktor symulatora
     */
    public ECS() {
        this.settings = new UIAndSimulationSettings(false, ToolBarInstrument.GOLD, RenderMode.NORMAL);
        this.systemList = new ArrayList<ECSSystem>();
        this.entitiesThreadedList = new HashMap<Entity, ArrayList<Callable<Void>>>();
        this.executor = Executors.newFixedThreadPool(50);
        this.entityList = new HashSet<Entity>();
    }

    /**
     * Rejestrowanie systemy w zbiorze system.
     *
     * @param a_system
     */
    public void registerSystem(ECSSystem a_system) {
        this.systemList.add(a_system);
    }

    /**
     * Dodanie nowego {@link Entity}. Ważne jest ich dodanie po rejestracji
     * wszystkich
     * system. Jest to dlatego, że tutaj tworzymy dla każdej nowej systemy Callable,
     * by móc to zrównołeglić. Oraz dodajemy tutaj tego {@link Entity} do zwykłego
     * {@link HashSet}. Jest to potrzebne by łatwo go potem zserializować do
     * {@link JSONArray} jego komponentów.
     *
     * @param a_entity dodawany {@link Entity}
     */
    public void addEntity(Entity a_entity) {
        ArrayList<Callable<Void>> threadedSystems = new ArrayList<Callable<Void>>();
        for (ECSSystem i : this.systemList) {
            threadedSystems.add(() -> {
                i.onFrameStart(a_entity);
                return null;
            });
        }
        this.entityList.add(a_entity);
        this.entitiesThreadedList.put(a_entity, threadedSystems);
    }

    /**
     * Usuwanie {@link Entity} z listy i jego {@link Callable} obiektami. Możemy to
     * łatwo zrobić przez to, że chronimy liste({@link ArrayList}) {@link Callable}
     * jako value w {@link HashMap}, a kluczem jest {@link Entity}.
     *
     * @param a_entity usuwany {@link Entity}
     */
    public void deleteEntity(Entity a_entity) {
        this.entitiesThreadedList.remove(a_entity);
    }

    /**
     * Zwracanie wszystkich {@link Entity}, które są w symulacji
     *
     * @return {@link Set} w którym są {@link Entity}
     */
    public Set<Entity> getEntityList() {
        return this.entitiesThreadedList.keySet();
    }

    /**
     * Główna metoda, która liczy następną iterację symulacji.
     */
    public void run() {
        try {
            HashSet<Callable<Void>> concatThreaded = new HashSet<Callable<Void>>();
            for (ArrayList<Callable<Void>> i : this.entitiesThreadedList.values()) {
                concatThreaded.addAll(i);
            }
            this.executor.invokeAll(concatThreaded);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Szukanie pierwszego(!) entity, któryby spełniał warunki, podane przez
     * {@link Predicate}
     *
     * @param a_filter lambda, służąca do filtracji
     * @return {@link Entity}, przy którym podany {@link Predicate} zwraca True
     */
    public Entity findFirstEntityByFilter(Predicate<Entity> a_filter) {
        for (Entity o : this.entitiesThreadedList.keySet()) {
            if (a_filter.test(o))
                return o;
        }
        return null;
    }

    /**
     * To samo, co i findFirstEntityByFilter, tylko szukamy wszystkich
     * {@link Entity}, przy których {@link Predicate} zwraca True.
     *
     * @param a_filter lambda, służąca do filtracji
     * @return {@link HashSet} z {@link Entity}, które spełnili podany filtr
     */
    public HashSet<Entity> findEntitiesByFilter(Predicate<Entity> a_filter) {
        HashSet<Entity> finList = new HashSet<Entity>();
        for (Entity o : this.entitiesThreadedList.keySet()) {
            if (a_filter.test(o))
                finList.add(o);
        }
        return finList;
    }

    public JSONObject toJSON() {
        JSONObject res = new JSONObject();
        JSONArray entities = new JSONArray();
        for (Entity i : this.entityList) {
            entities.put(i.toJSON());
        }
        res.put("entities", entities);
        res.put("settings", this.settings.toJSON());
        return res;
    }

    public void fromJSON(JSONObject save) throws JSONException {
        this.entityList.clear();
        this.entitiesThreadedList.clear();
        for (String key : save.keySet()) {
            switch (key) {
                case "settings":
                    this.settings.fromJSON(save.getJSONObject(key));
                    break;
                case "entities":
                    JSONArray entitiesToCreate = save.getJSONArray(key);
                    for (Object i : entitiesToCreate) {
                        JSONArray entityObj = (JSONArray) i;
                        this.addEntity(new Entity(entityObj));
                    }
                    break;
                default:
                    throw new JSONException("Unexpected key");
            }
        }
    }
}
