package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

import java.util.function.Consumer;
import java.util.function.Function;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;

import java.awt.Point;

public enum ToolBarInstrument {
  CHARGEP(
    "Add charge",
    a_ecs -> a_position -> {
      System.out.println("XD");
    }
  ),
  CHARGEM(
    "Subract charge",
    a_ecs -> a_position -> {
      ECS e = a_ecs;
      e.findFirstEntityByFilter(
        (a_entity) -> {
          PositionComponent position_in = (PositionComponent) a_entity.getFirstComponentOfType(PositionComponent.class);
          return (position_in.x == a_position.x && position_in.y == position_in.y);
        }
      );
    }
  ),
  TEMPP(
    "Add thermal energy",
    a_ecs -> a_position -> {
      
    }
  ),
  TEMPM(
    "Subract thermal energy",
    a_ecs -> position -> {
      
    }
  );
  private final String name;
  private final Function<ECS, Consumer<Point>> action;
  private ToolBarInstrument(
    String a_name,
    Function<ECS, Consumer<Point>> a_action
  ) {
    this.name = a_name;
    this.action = a_action;
  }
}
