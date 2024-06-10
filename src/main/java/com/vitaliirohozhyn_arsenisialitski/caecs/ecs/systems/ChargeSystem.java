package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ChargeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.TemperatureComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialType;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.Utils;

public class ChargeSystem extends ECSSystem {

    public ChargeSystem(ECS a_ecs) {
        super(a_ecs);
    }

    public void onFrameStart(Entity a_entity) {
        if (a_entity.getFirstComponentOfType(MaterialTypeComponent.class).materialType == MaterialType.VACUUM)
            return;
        PositionComponent pos = a_entity.getFirstComponentOfType(PositionComponent.class);
        ChargeComponent charge = a_entity.getFirstComponentOfType(ChargeComponent.class);
        MaterialTypeComponent own_sigma = a_entity.getFirstComponentOfType(MaterialTypeComponent.class);

        int x_pos = pos.x;
        int y_pos = pos.y;

        HashSet<Entity> neighbours = ecs.findEntitiesByFilter((a_entity_in) -> {
            if (a_entity == a_entity_in)
                return false;
            if (a_entity_in.getFirstComponentOfType(MaterialTypeComponent.class).materialType == MaterialType.VACUUM)
                return false;
            PositionComponent pos_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
            return ((x_pos + 1 == pos_in.x && y_pos == pos_in.y) ||
                    (x_pos - 1 == pos_in.x && y_pos == pos_in.y) ||
                    (y_pos + 1 == pos_in.y && x_pos == pos_in.x) ||
                    (y_pos - 1 == pos_in.y && x_pos == pos_in.x));
        });

        for (Entity i : neighbours) {
            ChargeComponent charge_in = i.getFirstComponentOfType(ChargeComponent.class);
            TemperatureComponent temp_in = i.getFirstComponentOfType(TemperatureComponent.class);
            MaterialTypeComponent sigma_in = i.getFirstComponentOfType(MaterialTypeComponent.class);
            double gradient_sigma = (sigma_in.materialType.conductivity + own_sigma.materialType.conductivity) *
                    (sigma_in.IsConductivityZero() ? 0 : 1) *
                    (own_sigma.IsConductivityZero() ? 0 : 1) / 2;
            double chargeToGive = (charge.charge - charge_in.charge) / gradient_sigma;

            if (gradient_sigma != 0) {
                charge_in.charge += chargeToGive;

                temp_in.temperature += Math.abs(chargeToGive * sigma_in.materialType.thermalConductivity / 1000);
                charge.charge -= chargeToGive;
            } else {
                break;
            }

        }
        if (!own_sigma.materialType.magnetic)
            return;
        // "Magnetism" effect. If the charge difference is too big, we will pull then
        // together
        Double searchRadius = Math.abs(charge.charge) / 5.0;
        double neededCulombForce = 5.0;
        HashSet<Entity> filteredForMagnetism = this.ecs.findEntitiesByFilter((a_entity_in) -> {
            if (neighbours.contains(a_entity_in) || a_entity_in == a_entity)
                return false; // We don't want the neighbours
            MaterialTypeComponent mat_type_in = a_entity_in.getFirstComponentOfType(MaterialTypeComponent.class);
            if (!mat_type_in.materialType.magnetic)
                return false;
            PositionComponent position_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
            Double distance = Utils.findDistanceBetweenTwoTiles(pos, position_in);
            if (distance > searchRadius)
                return false;
            ChargeComponent charge_in = a_entity_in.getFirstComponentOfType(ChargeComponent.class);
            Double culombForce = Math.abs(charge_in.charge - charge.charge) /
                    Math.pow(distance, 2);
            return (culombForce > neededCulombForce);
        });
        for (Entity i : filteredForMagnetism) {
            PositionComponent position_in = i.getFirstComponentOfType(PositionComponent.class);
            Integer deltaX = position_in.x - pos.x;
            Integer deltaY = position_in.y - pos.y;
            Integer deltaPosX = Math.abs(deltaX) >= Math.abs(deltaY) ? (int) Math.signum(-deltaX) : 0;
            Integer deltaPosY = Math.abs(deltaY) >= Math.abs(deltaX) ? (int) Math.signum(-deltaY) : 0;
            Boolean entityDoesntExistAtWantedPosition = Utils.getEntityAtCoordinates(this.ecs,
                    position_in.x + deltaPosX, position_in.y + deltaPosY) == null;
            if (entityDoesntExistAtWantedPosition) {
                position_in.x += deltaPosX;
                position_in.y += deltaPosY;
                continue;
            }
            if (deltaPosX != deltaPosY)
                continue;
            Random rand = new Random();
            boolean orientationChoose = rand.nextBoolean();
            deltaPosX = orientationChoose ? deltaPosX : 0;
            deltaPosY = orientationChoose ? 0 : deltaPosY;
            if (Utils.getEntityAtCoordinates(this.ecs, position_in.x + deltaPosX, position_in.y + deltaPosY) == null) {
                position_in.x += deltaPosX;
                position_in.y += deltaPosY;
                continue;
            }
            deltaPosX = orientationChoose ? 0 : deltaPosX;
            deltaPosY = orientationChoose ? deltaPosY : 0;
            if (Utils.getEntityAtCoordinates(this.ecs, position_in.x + deltaPosX, position_in.y + deltaPosY) == null) {
                position_in.x += deltaPosX;
                position_in.y += deltaPosY;
                continue;
            }
        }
    }
}
