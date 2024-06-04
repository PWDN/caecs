package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import com.vitaliirohozhyn_arsenisialitski.caecs.graphics.RenderMode;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.ToolBarInstrument;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.UIAndSimulationSettings;

public class ECS {
    private final ArrayList<ECSSystem> systemList;
    private final ExecutorService executor;
    private HashMap<Entity, ArrayList<Callable<Void>>> entitiesThreadedList;
    public final UIAndSimulationSettings settings;

    public ECS(float a_timeBetweenIterations) {
        this.settings = new UIAndSimulationSettings(false, ToolBarInstrument.GOLD, a_timeBetweenIterations,
                RenderMode.NORMAL);
        this.systemList = new ArrayList<ECSSystem>();
        this.entitiesThreadedList = new HashMap<Entity, ArrayList<Callable<Void>>>();
        this.executor = Executors.newFixedThreadPool(15);
    }

    public void registerSystem(ECSSystem a_system) {
        this.systemList.add(a_system);
    }

    public void addEntity(Entity a_entity) {
        ArrayList<Callable<Void>> threadedSystems = new ArrayList<Callable<Void>>();
        for (ECSSystem i : this.systemList) {
            threadedSystems.add(() -> {
                i.onFrameStart(a_entity);
                return null;
            });
        }
        this.entitiesThreadedList.put(a_entity, threadedSystems);
    }

    public void deleteEntity(Entity a_entity) {
        this.entitiesThreadedList.remove(a_entity);
    }

    public Set<Entity> getEntityList() {
        return this.entitiesThreadedList.keySet();
    }

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
        // for (ECSSystem i : this.systemList) {
        // for (Entity o : this.entityList) {
        // i.onFrameStart(o);
        // }
        // i.onFrameStartBatched(this.entityList);
        // }
        // for (ECSSystem i : this.systemList) {
        // for (Entity o : this.entityList) {
        // i.onFrameEnd(o);
        // }
        // i.onFrameEndBatched(this.entityList);
        // }
    }

    public Entity findFirstEntityByFilter(Predicate<Entity> a_filter) {
        for (Entity o : this.entitiesThreadedList.keySet()) {
            if (a_filter.test(o))
                return o;
        }
        return null;
    }

    public HashSet<Entity> findEntitiesByFilter(Predicate<Entity> a_filter) {
        HashSet<Entity> finList = new HashSet<Entity>();
        for (Entity o : this.entitiesThreadedList.keySet()) {
            if (a_filter.test(o))
                finList.add(o);
        }
        return finList;
    }
}
