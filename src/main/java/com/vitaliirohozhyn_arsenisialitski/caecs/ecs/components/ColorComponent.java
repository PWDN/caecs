package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import java.awt.Color;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;

public class ColorComponent implements Component {
  public Color color;
  public ColorComponent(Color a_color) {
    this.color = a_color;
}
  public void onAdd() {}
  public void onDelete() {}
  public String toString() {
    return this.color.toString();
  }
}
