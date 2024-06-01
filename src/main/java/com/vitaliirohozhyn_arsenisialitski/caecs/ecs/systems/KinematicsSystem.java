package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialStateComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MotionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialState;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialType;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.PhysicsConstant;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.Utils;

public class KinematicsSystem extends ECSSystem {
    public KinematicsSystem(ECS a_ecs) {
        super(a_ecs);
    };

    public void onFrameStart(Entity a_entity) {
        // return;
        MaterialTypeComponent mat = a_entity.getFirstComponentOfType(MaterialTypeComponent.class);
        MaterialStateComponent st = a_entity.getFirstComponentOfType(MaterialStateComponent.class);
        PositionComponent position = (PositionComponent) a_entity.getFirstComponentOfType(PositionComponent.class);
        MotionComponent motion = a_entity.getFirstComponentOfType(MotionComponent.class);
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

                HashSet<Entity> TilesOnLevelBelow = this.ecs.findEntitiesByFilter((a_entity_in) -> {
                    PositionComponent position_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
                    return (position.y + 1 == position_in.y);
                });
                PositionComponent lineStart = null;
                PositionComponent lineEnd = null;
                for (Entity i : TilesOnLevelBelow) { // finding the borders
                    MaterialStateComponent matStComp = i.getFirstComponentOfType(MaterialStateComponent.class);
                    if (matStComp.materialState == MaterialState.LIQUID)
                        continue;
                    PositionComponent position_in = i.getFirstComponentOfType(PositionComponent.class);
                    if (position_in.x < position.x) {
                        if (lineStart == null) {
                            lineStart = position_in;
                            continue;
                        }
                        // if (lineStart.x > )
                    }
                }
                // PositionComponent pos =
                // a_entity.getFirstComponentOfType(PositionComponent.class);
                // int x_pos = pos.x;
                // int y_pos = pos.y;

                // HashSet<Entity> neighbours = ecs.findEntitiesByFilter((a_entity_in) -> {
                // if (a_entity_in
                // .getFirstComponentOfType(MaterialTypeComponent.class).materialType ==
                // MaterialType.VACUUM)
                // return false;
                // PositionComponent pos_in =
                // a_entity_in.getFirstComponentOfType(PositionComponent.class);
                // return ((x_pos + 1 == pos_in.x && y_pos == pos_in.y) ||
                // (x_pos - 1 == pos_in.x && y_pos == pos_in.y) ||
                // (y_pos + 1 == pos_in.y && x_pos == pos_in.x));
                // });
                // if (neighbours.size() == 3)
                // break;
                // if (this.ecs.findFirstEntityByFilter(
                // (a_entity_in) -> {
                // PositionComponent position_in = a_entity_in
                // .getFirstComponentOfType(PositionComponent.class);
                // return (position_in.y == position.y + 1 && position_in.x == position.x);
                // }) == null) {
                // position.y += 1;
                // } else {
                // if (this.ecs.findFirstEntityByFilter(
                // (a_entity_in) -> {
                // PositionComponent position_in = a_entity_in.getFirstComponentOfType(
                // PositionComponent.class);
                // return (position_in.y == position.y + 1 && position_in.x == position.x +
                // cast);
                // }) != null) {
                // // position.y += 1;
                // position.x = position.x + cast;
                // } else if ((this.ecs.findFirstEntityByFilter(
                // (a_entity_in) -> {
                // PositionComponent position_in = a_entity_in.getFirstComponentOfType(
                // PositionComponent.class);
                // return (position_in.y == position.y && position_in.x == position.x + cast);
                // }) == null)) {
                // position.y += 1;
                // position.x = position.x + cast;

                // }
                // }
                break;

            default: // default powder psychics
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
        if (this.ecs.settings.physicsEnabled && mat.materialType != MaterialType.WALL) {
            if (Utils.getEntityAtCoordinates(this.ecs, position.x, position.y + 1) == null) {
                // motion.acceleration.y += PhysicsConstant.FREEFALLACCELERATION;
                motion.acceleration.y += 0.003;
            } else { // standing on something
                motion.acceleration.y = 0;
                motion.velocity.y = 0;
            }
        }
        // if (motion.acceleration.x <= 0.000000001 && motion.acceleration.y <=
        // 0.000000001 &&
        // motion.velocity.x <= 0.000001 && motion.velocity.y <= 0.00001)
        // return;
        // // Reacting basing off of current entitiy's velocity and acceleration
        // motion.velocity = Utils.vectorSum(motion.velocity,
        // Utils.vectorScale(motion.acceleration,
        // this.ecs.settings.timeBetweenIterations));
        // Point2D.Float newpos = Utils.vectorScale(motion.velocity,
        // this.ecs.settings.timeBetweenIterations / 100);
        // int newPosX = Math.round(newpos.x);
        // int newPosY = Math.round(newpos.y);
        // HashSet<java.awt.Point> pointsInTragectory = Utils.drawLine(new
        // java.awt.Point(position.x, position.y),
        // new java.awt.Point(position.x + newPosX, position.y + newPosY));
        // HashSet<Entity> foundEntities = ecs.findEntitiesByFilter((a_entity_in) -> {
        // if (a_entity_in == a_entity)
        // return false;
        // PositionComponent pos_in =
        // a_entity_in.getFirstComponentOfType(PositionComponent.class);
        // return pointsInTragectory.contains(new java.awt.Point(pos_in.x, pos_in.y));
        // });
        // if (foundEntities.size() == 0) {
        // position.x += newPosX;
        // position.y += newPosY;
        // } else {
        // ArrayList<Entity> forSort = new ArrayList<Entity>(foundEntities);
        // Collections.sort(forSort, (elem1, elem2) -> {
        // PositionComponent pos1 =
        // elem1.getFirstComponentOfType(PositionComponent.class);
        // PositionComponent pos2 =
        // elem2.getFirstComponentOfType(PositionComponent.class);
        // double dist1 = Point2D.distance(position.x, position.y, pos1.x, pos1.y);
        // double dist2 = Point2D.distance(position.x, position.y, pos2.x, pos2.y);
        // return dist1 > dist2 ? 1 : -1;
        // });
        // java.awt.Point vectorOfReduction = new java.awt.Point(0, 0);
        // vectorOfReduction.x = Math.abs(newPosX) >= Math.abs(newPosY) ? (int)
        // Math.signum(-newPosX) : 0;
        // vectorOfReduction.y = Math.abs(newPosY) >= Math.abs(newPosX) ? (int)
        // Math.signum(-newPosY) : 0;
        // PositionComponent colPos =
        // forSort.get(0).getFirstComponentOfType(PositionComponent.class);
        // System.out.println(colPos.toString());
        // System.out.println(vectorOfReduction.toString());
        // if (vectorOfReduction.x != 0 || vectorOfReduction.y != 0) {
        // position.x = colPos.x + vectorOfReduction.x;
        // position.y = colPos.y + vectorOfReduction.y;
        // }
        // }
        // motion.acceleration.x = Utils.clamp(motion.acceleration.x,
        // -PhysicsConstant.MAXACCELERATION.floatValue(),
        // PhysicsConstant.MAXACCELERATION.floatValue());
        // motion.acceleration.y = Utils.clamp(motion.acceleration.y,
        // -PhysicsConstant.MAXACCELERATION.floatValue(),
        // PhysicsConstant.MAXACCELERATION.floatValue());
        // motion.velocity.x = Utils.clamp(motion.velocity.x,
        // -PhysicsConstant.MAXSPEED.floatValue(),
        // PhysicsConstant.MAXSPEED.floatValue());
        // motion.velocity.y = Utils.clamp(motion.velocity.y,
        // -PhysicsConstant.MAXSPEED.floatValue(),
        // PhysicsConstant.MAXSPEED.floatValue());
        // end
    }
}
