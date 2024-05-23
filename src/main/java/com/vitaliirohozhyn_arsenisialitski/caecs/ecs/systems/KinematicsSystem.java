package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import java.util.HashSet;
import java.util.Random;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ChargeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialStateComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialType;

public class KinematicsSystem extends ECSSystem {
  public KinematicsSystem(ECS a_ecs) {
    super(a_ecs);
  };
  public void onFrameStart(Entity a_entity) {
    //return;
    MaterialTypeComponent mat = a_entity.getFirstComponentOfType(MaterialTypeComponent.class);
    MaterialStateComponent st = a_entity.getFirstComponentOfType(MaterialStateComponent.class);
    PositionComponent position = (PositionComponent) a_entity.getFirstComponentOfType(PositionComponent.class);
    switch (st.materialState) {
      case SOLID:
        break;
      case GAS: // inversed powder
        if (this.ecs.findFirstEntityByFilter(
                (a_entity_in) -> {
                    PositionComponent position_in = a_entity_in
                            .getFirstComponentOfType(PositionComponent.class);
                    return (position_in.y == position.y - 1 && position_in.x == position.x);
                }) == null) {
            position.y -= 1;
        } else {
            Random rand = new Random();
            boolean side = rand.nextBoolean();
            int cast = side ? 1 : -1;
            if (this.ecs.findFirstEntityByFilter(
                    (a_entity_in) -> {
                        PositionComponent position_in = a_entity_in.getFirstComponentOfType(
                                PositionComponent.class);         
                        return (position_in.y == position.y - 1 && position_in.x == position.x + cast);
                    }) == null) {
                position.y -= 1;
                position.x = position.x + cast;
            }
        }
          break; 
        case LIQUID: // dynamic powder
            PositionComponent pos = a_entity.getFirstComponentOfType(PositionComponent.class);
            int x_pos = pos.x;
            int y_pos = pos.y;


            HashSet<Entity> neighbours = ecs.findEntitiesByFilter((a_entity_in) -> {
            if (a_entity_in.getFirstComponentOfType(MaterialTypeComponent.class).materialType == MaterialType.VACUUM) return false;
            PositionComponent pos_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
            return(
                (x_pos + 1 == pos_in.x && y_pos == pos_in.y) ||
                (x_pos - 1 == pos_in.x && y_pos == pos_in.y) ||
                (y_pos + 1 == pos_in.y && x_pos == pos_in.x)
            );
            });
          if (neighbours.size() == 3) break;
          if (this.ecs.findFirstEntityByFilter(
                  (a_entity_in) -> {
                      PositionComponent position_in = a_entity_in
                              .getFirstComponentOfType(PositionComponent.class);
                      return (position_in.y == position.y + 1 && position_in.x == position.x);
                  }) == null) {
              position.y += 1;
          } else {
              //Random rand = new Random();
              //boolean side = true; //rand.nextBoolean();
              int cast = 1;   //side ? 1 : -1;
              if (this.ecs.findFirstEntityByFilter(
                      (a_entity_in) -> {
                          PositionComponent position_in = a_entity_in.getFirstComponentOfType(
                                  PositionComponent.class);         
                          return (position_in.y == position.y +1 && position_in.x == position.x + cast);
                      }) != null) {
                  //position.y += 1;
                  position.x = position.x + cast;
              }
              else if((this.ecs.findFirstEntityByFilter(
                (a_entity_in) -> {
                    PositionComponent position_in = a_entity_in.getFirstComponentOfType(
                            PositionComponent.class);         
                    return (position_in.y == position.y && position_in.x == position.x + cast);
                }) == null)){
                  position.y += 1;
                  position.x = position.x + cast;
                
              }
          }
            break;
     
      default:  // default powder psychics
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
      }
        break;
    }
       // end 
  }
}
