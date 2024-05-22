package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import java.util.Random;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
public class KinematicsSystem extends ECSSystem {
  public KinematicsSystem(ECS a_ecs) {
    super(a_ecs);
  };
  public void onFrameStart(Entity a_entity) {
    //return;
    MaterialTypeComponent mat = a_entity.getFirstComponentOfType(MaterialTypeComponent.class);
    if(mat.materialType.name == "Air") return;
      PositionComponent position = (PositionComponent) a_entity.getFirstComponentOfType(PositionComponent.class);
      if (this.ecs.findFirstEntityByFilter(
              (a_entity_in) -> {
                  PositionComponent position_in = a_entity_in
                          .getFirstComponentOfType(PositionComponent.class);
                  return (position_in.y == position.y + 1 && position_in.x == position.x);
              }) == null) {
          position.y += 1;
      } else {
          Random rand = new Random();
          boolean side = rand.nextBoolean();
          int cast = side ? 1 : -1;
          if (this.ecs.findFirstEntityByFilter(
                  (a_entity_in) -> {
                      PositionComponent position_in = a_entity_in.getFirstComponentOfType(
                              PositionComponent.class);         
                      return (position_in.y == position.y + 1 && position_in.x == position.x + cast);
                  }) == null) {
              position.y += 1;
              position.x = position.x + cast;
          }
      } // end 
  }
}
