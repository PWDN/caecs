package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

public class ECS {
    private final ArrayList<ECSSystem> systemList;
    private final ArrayList<Entity> entityList;

    public ECS() {
        this.systemList = new ArrayList<ECSSystem>();
        this.entityList = new ArrayList<Entity>();
    }

    public void registerSystem(ECSSystem a_system) {
        this.systemList.add(a_system);
    }

    public void addEntity(Entity a_entity) {
        this.entityList.add(a_entity);
    }

    public void deleteEntity(Entity a_entity) {
        this.entityList.remove(a_entity);
    }

    public ArrayList<Entity> getEntityList() {
        return this.entityList;
    }

    public void run() {
        for (ECSSystem i : this.systemList) {
            for (Entity o : this.entityList) {
                i.onFrameStart(o);
            }
        }
        for (ECSSystem i : this.systemList) {
            for (Entity o : this.entityList) {
                i.onFrameEnd(o);
            }
        }
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
