package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.IntStream;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialStateComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.Utils;

public class KinematicsSystem extends ECSSystem {
    public KinematicsSystem(ECS a_ecs) {
        super(a_ecs);
    };

    public void run(Entity a_entity) {
        if (!this.ecs.settings.gravityEnabled)
            return;
        MaterialStateComponent st = a_entity.getFirstComponentOfType(MaterialStateComponent.class);
        PositionComponent position = (PositionComponent) a_entity.getFirstComponentOfType(PositionComponent.class);
        switch (st.materialState) {
            case SOLID: // METAL GEAR
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
                if (Utils.getEntityAtCoordinates(this.ecs, position.x, position.y + 1) == null) {
                    position.y += 1;
                    return;
                }
                Integer wallExistsOnSide = 0;
                Entity wallOnLeft = Utils.getEntityAtCoordinates(this.ecs, position.x - 1, position.y);
                Entity wallOnRight = Utils.getEntityAtCoordinates(this.ecs, position.x + 1, position.y);
                if (wallOnLeft != null) {
                    wallExistsOnSide = -1;
                }
                if (wallOnRight != null) {
                    if (wallExistsOnSide != 0) {
                        return;
                    }
                    wallExistsOnSide = 1;
                }
                HashMap<Integer, Integer> borders = Utils.getBordersOfTypeFromCenter(this.ecs, position.x,
                        position.y + 1);
                Integer lineStart = borders.get(0);
                Integer lineEnd = borders.get(1);
                lineStart = wallExistsOnSide == -1 ? position.x : lineStart;
                lineEnd = wallExistsOnSide == 1 ? position.x : lineEnd;
                HashSet<Integer> vacantX = new HashSet<Integer>();
                final Integer wantedY = position.y + 1;

                IntStream.range(lineStart + 1, lineEnd)
                        .parallel()
                        .forEach(n -> {
                            if (Utils.getEntityAtCoordinates(this.ecs, n, wantedY) == null) {
                                vacantX.add(n);
                            }
                        });
                if (vacantX.size() == 0) {
                    HashMap<Integer, Integer> borderOnLine = Utils.getBordersOfTypeFromCenter(this.ecs, position.x,
                            position.y);
                    final Integer wantedY_in = position.y;
                    HashSet<Integer> vacantX_in = new HashSet<Integer>();
                    IntStream.range(borderOnLine.get(0) + 1, borderOnLine.get(1))
                            .parallel()
                            .forEach(n -> {
                                if (Utils.getEntityAtCoordinates(this.ecs, n, wantedY_in) == null) {
                                    vacantX_in.add(n);
                                }
                            });
                    if (vacantX_in.size() == 0) {
                        return;
                    }
                    Integer nearest = null;
                    Integer distance = null;
                    for (Integer i : vacantX_in) {
                        if (nearest == null) {
                            nearest = i;
                            distance = Math.abs(position.x - i);
                            continue;
                        }
                        Integer newDist = Math.abs(position.x - i);
                        if (newDist < distance) {
                            nearest = i;
                            distance = newDist;
                            continue;
                        }
                    }
                    if (borderOnLine.get(0) != -1 && borderOnLine.get(1) != 21) {
                        Random rand = new Random();
                        boolean side = rand.nextBoolean();
                        final int cast = side ? 1 : -1;
                        if ((wallOnLeft == null && cast == -1) || (wallOnRight == null && cast == 1)) {
                            position.x += cast;
                            return;
                        } else {
                            position.x -= cast;
                            return;
                        }
                    }
                    position.x = nearest;
                    return;
                }
                Integer nearest = null;
                Integer distance = null;
                for (Integer i : vacantX) {
                    if (nearest == null) {
                        nearest = i;
                        distance = Math.abs(position.x - i);
                        continue;
                    }
                    Integer newDist = Math.abs(position.x - i);
                    if (newDist < distance) {
                        nearest = i;
                        distance = newDist;
                        continue;
                    }
                }
                position.y += 1;
                position.x = nearest;
                break;

            case POWDER: // default powder psychics
                if (Utils.getEntityAtCoordinates(this.ecs, position.x, position.y + 1) == null) {
                    position.y += 1;
                    return;
                }
                Random rand = new Random();
                boolean side = rand.nextBoolean();
                final int cast = side ? 1 : -1;
                if (this.ecs.findFirstEntityByFilter((a_entity_in) -> {
                    PositionComponent position_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
                    return ((position.x + cast == position_in.x &&
                            position.y + 1 == position_in.y) ||
                            (position.x + cast == position_in.x &&
                                    position.y == position_in.y));
                }) == null) {
                    position.x += cast;
                    position.y += 1;
                    return;
                }
                final int castReversed = side ? -1 : 1;
                if (this.ecs.findFirstEntityByFilter((a_entity_in) -> {
                    PositionComponent position_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
                    return ((position.x + castReversed == position_in.x &&
                            position.y + 1 == position_in.y) ||
                            (position.x + castReversed == position_in.x &&
                                    position.y == position_in.y));
                }) == null) {
                    position.x += castReversed;
                    position.y += 1;
                    return;
                }
                return;
            default:
                return;
        }
    }
}
