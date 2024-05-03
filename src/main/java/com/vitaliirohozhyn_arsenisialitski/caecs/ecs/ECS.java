package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ECS {
  private final ArrayList<Consumer<Entity>> systemList;
  private final ArrayList<Entity> entityList;
  public ECS() {
    this.systemList = new ArrayList<Consumer<Entity>>();
    this.entityList = new ArrayList<Entity>();
  }
  public void registerSystem(Consumer<Entity> a_system) {
    this.systemList.add(a_system);
  }
  public void addEntity(Entity a_entity) {
    this.entityList.add(a_entity);
  }
  public void run() {
    for (Consumer<Entity> i : this.systemList) {
      for (Entity o : this.entityList) {
        i.accept(o);
      }
    }
  }
  public Entity findFirstEntityByFilter(Predicate<Entity> a_filter) {
    for (Entity o : this.entityList) {
      if (a_filter.test(o)) return o;
    }
    return null;
  }
}
