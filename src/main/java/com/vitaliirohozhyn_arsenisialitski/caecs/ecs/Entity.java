package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

import java.util.ArrayList;

public class Entity {
  private final ArrayList<Component> componentList;
  public Entity() {
    this.componentList = new ArrayList<Component>();
  }
  public Entity(Component[] a_componentList) {
    this.componentList = new ArrayList<Component>();
    for (Component i : a_componentList) {
      this.addComponent(i);
    }
  }
  public void addComponent(Component a_component) {
    this.componentList.add(a_component);
  }
  public boolean doesEntityHasComponentOfType(Class<? extends Component> a_class) {
    for (Component i : this.componentList) {
      if (a_class == i.getClass()) return true;
    }
    return false;
  }
  public Component getFirstComponentOfType(Class<? extends Component> a_class) {
    for(Component i : this.componentList) {
      if (a_class == i.getClass()) return i;
    }
    throw new RuntimeException("Unhandled search of component in entity");
  }
  public String toString() {
    StringBuilder build = new StringBuilder();
    for(Component i: this.componentList) {
      build.append(i.toString());
      if (i != this.componentList.get(this.componentList.size() - 1)) build.append("\n");
    }
    return build.toString();
  }
}
