package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.function.Consumer;
import java.util.function.Function;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ChargeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ColorComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MotionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.TemperatureComponent;

public enum ToolBarInstrument {
    CHARGEP(
            "Add charge",
            a_ecs -> a_position -> {
                Entity found =  getTileAtPosition(a_ecs, a_position);
                if (found == null)
                    return;
                if (!found.doesEntityHasComponentOfType(ChargeComponent.class))
                    return;
                ChargeComponent charge = found.getFirstComponentOfType(ChargeComponent.class);
                charge.charge += 1;
            }),
    CHARGEM(
            "Subract charge",
            a_ecs -> a_position -> {
                Entity found =  getTileAtPosition(a_ecs, a_position);
                if (found == null)
                    return;
                if (!found.doesEntityHasComponentOfType(ChargeComponent.class))
                    return;
                ChargeComponent charge = found.getFirstComponentOfType(ChargeComponent.class);
                charge.charge -= 1;
            }),
    TEMPP(
            "Add thermal energy",
            a_ecs -> a_position -> {
                Entity found = a_ecs.findFirstEntityByFilter(
                        (a_entity) -> {
                            PositionComponent position_in = (PositionComponent) a_entity
                                    .getFirstComponentOfType(PositionComponent.class);
                            return (position_in.x == a_position.x && position_in.y == position_in.y);
                        });
                if (found != null) {
                    if (!found.doesEntityHasComponentOfType(TemperatureComponent.class))
                        return;
                    TemperatureComponent temperature = found.getFirstComponentOfType(TemperatureComponent.class);
                    temperature.temperature += 1;
                }
            }),
    TEMPM(
            "Subract thermal energy",
            a_ecs -> a_position -> {
                Entity found = a_ecs.findFirstEntityByFilter(
                        (a_entity) -> {
                            PositionComponent position_in = (PositionComponent) a_entity
                                    .getFirstComponentOfType(PositionComponent.class);
                            return (position_in.x == a_position.x && position_in.y == position_in.y);
                        });
                if (found != null) {
                    if (!found.doesEntityHasComponentOfType(TemperatureComponent.class))
                        return;
                    TemperatureComponent temperature = found.getFirstComponentOfType(TemperatureComponent.class);
                    temperature.temperature -= 1;
                }
            }),
    DELETE(
            "Delete tile",
            a_ecs -> a_position -> {
                Entity tile = getTileAtPosition(a_ecs, a_position);
                if (tile == null)
                    return;
                a_ecs.deleteEntity(tile);
            }),
    WALL(           "Add wall",
            a_ecs -> a_position -> {
                if (getTileAtPosition(a_ecs,a_position)!= null)
                    return;
            Entity ent = new Entity();
            ent.addComponent(new PositionComponent(a_position.x, a_position.y));
            ent.addComponent(new TemperatureComponent(300, false));
            ent.addComponent(new ChargeComponent(0));
            ent.addComponent(new MotionComponent(new Point2D.Float(0,0),new Point2D.Float(0,0)));
            ent.addComponent(new ColorComponent(
                Utils.generateRandomColorFromOriginal(Color.BLACK, 1)));
            ent.addComponent(new MaterialTypeComponent(MaterialType.WALL));
            a_ecs.addEntity(ent);
            }),
    WATER(           "Add water",
            a_ecs -> a_position -> {
                if (getTileAtPosition(a_ecs,a_position)!= null)
                    return;
            Entity ent = new Entity();
            ent.addComponent(new PositionComponent(a_position.x, a_position.y));
            ent.addComponent(new TemperatureComponent(300, false));
            ent.addComponent(new ChargeComponent(0));
            ent.addComponent(new MotionComponent(new Point2D.Float(0,0),new Point2D.Float(0,0)));
            ent.addComponent(new ColorComponent(
                Utils.generateRandomColorFromOriginal(Color.BLUE, 2)));
            ent.addComponent(new MaterialTypeComponent(MaterialType.WATER));
            a_ecs.addEntity(ent);
            }),
    GOLD(
            "Add gold",
            a_ecs -> a_position -> {
                if (getTileAtPosition(a_ecs, a_position) != null)
                    return;
                Entity ent = new Entity();
                ent.addComponent(new PositionComponent(a_position.x, a_position.y));
                ent.addComponent(new TemperatureComponent(300, false));
                ent.addComponent(new ChargeComponent(0));
                ent.addComponent(new MotionComponent(new Point2D.Float(0, 0), new Point2D.Float(0, 0)));
                ent.addComponent(new ColorComponent(
                        Utils.generateRandomColorFromOriginal(Color.YELLOW, 2)));
                ent.addComponent(new MaterialTypeComponent(MaterialType.GOLD));
                a_ecs.addEntity(ent);
            }),
    AIR(
            "Add air",
            a_ecs -> a_position -> {
                if (getTileAtPosition(a_ecs, a_position) != null)
                    return;
                Entity ent = new Entity();
                ent.addComponent(new PositionComponent(a_position.x, a_position.y));
                ent.addComponent(new TemperatureComponent(300, false));
                ent.addComponent(new ChargeComponent(0));
                ent.addComponent(new MotionComponent(new Point2D.Float(0, 0), new Point2D.Float(0, 0)));
                ent.addComponent(new ColorComponent(Utils.generateRandomColorFromOriginal(Color.WHITE, 2)));
                ent.addComponent(new MaterialTypeComponent(MaterialType.AIR));
                a_ecs.addEntity(ent);
            });

    public final String name;
    public final Function<ECS, Consumer<Point>> action;

    private static Entity getTileAtPosition(ECS a_ecs, Point a_position) {
        Entity found = a_ecs.findFirstEntityByFilter(
                (a_entity) -> {
                    PositionComponent position_in = (PositionComponent) a_entity
                            .getFirstComponentOfType(PositionComponent.class);
                    return (position_in.x == a_position.x && a_position.y == position_in.y);
                });
        return found;
    }

    private ToolBarInstrument(
            String a_name,
            Function<ECS, Consumer<Point>> a_action) {
        this.name = a_name;
        this.action = a_action;
    }
}
