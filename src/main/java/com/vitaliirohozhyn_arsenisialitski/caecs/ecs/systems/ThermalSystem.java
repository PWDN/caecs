package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialStateComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.TemperatureComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialState;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialType;

import java.util.HashSet;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;

public class ThermalSystem extends ECSSystem {

    public ThermalSystem(ECS a_ecs) {
        super(a_ecs);
    }

    public void onFrameStart(Entity a_entity) {
        if (a_entity.getFirstComponentOfType(MaterialTypeComponent.class).materialType == MaterialType.VACUUM)
            return;
        PositionComponent pos = a_entity.getFirstComponentOfType(PositionComponent.class);
        TemperatureComponent local_temp = a_entity.getFirstComponentOfType(TemperatureComponent.class);
        MaterialTypeComponent own_props = a_entity.getFirstComponentOfType(MaterialTypeComponent.class);
        MaterialStateComponent own_state = a_entity.getFirstComponentOfType(MaterialStateComponent.class);

        int x_pos = pos.x;
        int y_pos = pos.y;

        HashSet<Entity> neighbours = ecs.findEntitiesByFilter((a_entity_in) -> {
            if (a_entity_in.getFirstComponentOfType(MaterialTypeComponent.class).materialType == MaterialType.VACUUM)
                return false;
            PositionComponent pos_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
            return ((x_pos + 1 == pos_in.x && y_pos == pos_in.y) ||
                    (x_pos - 1 == pos_in.x && y_pos == pos_in.y) ||
                    (y_pos + 1 == pos_in.y && x_pos == pos_in.x) ||
                    (y_pos - 1 == pos_in.y && x_pos == pos_in.x));
        });
        if (neighbours.size() == 0)
            return;

        for (Entity i : neighbours) {
            TemperatureComponent temp_in = i.getFirstComponentOfType(TemperatureComponent.class);
            MaterialTypeComponent props_in = i.getFirstComponentOfType(MaterialTypeComponent.class);
            double gradient_sigma = (props_in.materialType.thermalConductivity
                    + own_props.materialType.thermalConductivity) *
                    (props_in.IsThermConductivityZero() ? 0 : 1) *
                    (own_props.IsThermConductivityZero() ? 0 : 1) / 2;

            if (gradient_sigma != 0) {

                temp_in.temperature += ((local_temp.temperature - temp_in.temperature) / (gradient_sigma / 100));
                local_temp.temperature -= ((local_temp.temperature - temp_in.temperature) / (gradient_sigma / 100));

            }
            // if(local_temp.temperature < own_props.materialType.meltingPoint){
            // own_state.materialState = MaterialState.SOLID;
            // } else if((local_temp.temperature >= own_props.materialType.meltingPoint) &&
            // (local_temp.temperature < own_props.materialType.boillingPoint)){
            // own_state.materialState = MaterialState.LIQUID;
            // } else if(local_temp.temperature >= own_props.materialType.boillingPoint){
            // own_state.materialState = MaterialState.GAS;
            // } // add plasma

        }
    }
}
