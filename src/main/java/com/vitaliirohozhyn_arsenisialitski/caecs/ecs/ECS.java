package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class ECS {
    private final ArrayList<ECSSystem> systemList;
    private final ArrayList<Entity> entityList;
    private final ExecutorService executor;
    private ArrayList <Callable<Void>> runnables;
    public ECS() {
        this.systemList = new ArrayList<ECSSystem>();
        this.entityList = new ArrayList<Entity>();
        this.executor = Executors.newFixedThreadPool(15);
        this.runnables = new ArrayList<Callable<Void>>();
    }

    public void registerSystem(ECSSystem a_system) {
        this.systemList.add(a_system);
    }

    public void addEntity(Entity a_entity) {
        try {
            this.entityList.add(a_entity);
            for (ECSSystem i: this.systemList) {
                runnables.add(() -> {
        				    i.onFrameStart(a_entity);
        				    return null;
				});
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteEntity(Entity a_entity) {
        this.entityList.remove(a_entity);
    }

    public ArrayList<Entity> getEntityList() {
        return this.entityList;
    }

    public void run() {
        try{
            this.executor.invokeAll(this.runnables);
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
        for (Entity o : this.entityList) {
            if (a_filter.test(o))
                return o;
        }
        return null;
    }
    
    public HashSet<Entity> findEntitiesByFilter(Predicate<Entity> a_filter) {
        HashSet<Entity> finList = new HashSet<Entity>();
        for (Entity o: this.entityList) {
            if (a_filter.test(o)) finList.add(o);
        }
        return finList;
    }
}
