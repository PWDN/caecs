package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import java.util.ArrayList;
import java.util.HashSet;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ChargeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialType;

public class ChargeSystem extends ECSSystem {

    public ChargeSystem(ECS a_ecs) {
        super(a_ecs);
    }
    public void onFrameStart(Entity a_entity) {
        if(a_entity.getFirstComponentOfType(MaterialTypeComponent.class).materialType == MaterialType.VACUUM ) return;
        PositionComponent pos = a_entity.getFirstComponentOfType(PositionComponent.class);
        ChargeComponent charge = a_entity.getFirstComponentOfType(ChargeComponent.class);
        
        int x_pos = pos.x;
        int y_pos = pos.y;
        double local_charge = charge.charge;

        HashSet<Entity> neighbours = ecs.findEntitiesByFilter((a_entity_in) -> {
            if (a_entity_in.getFirstComponentOfType(MaterialTypeComponent.class).materialType == MaterialType.VACUUM) return false;
            PositionComponent pos_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
            return(
                (x_pos + 1 == pos_in.x && y_pos == pos_in.y) ||
                (x_pos - 1 == pos_in.x && y_pos == pos_in.y) ||
                (y_pos + 1 == pos_in.y && x_pos == pos_in.x) ||
                (y_pos - 1 == pos_in.y && x_pos == pos_in.x)
            );
        });

        if (neighbours.size() == 0) return;
        
        Double toGive = 0.0;
        for(Entity i: neighbours) {
            ChargeComponent charge_in = i.getFirstComponentOfType(ChargeComponent.class);
            if (charge_in.charge < local_charge - 10) {
                toGive += (local_charge - charge_in.charge) / (10);
            } else {
                neighbours.remove(i);
            }
        }

        for(Entity i: neighbours) {
            ChargeComponent charge_in = i.getFirstComponentOfType(ChargeComponent.class);
            charge_in.charge += (local_charge - charge_in.charge) / (10);
            charge.charge -= (local_charge - charge_in.charge) / (10);
        }
    }
    public void onFrameEndBatched(final ArrayList<Entity> a_entity) {
        System.out.println(a_entity.size());
    }
}
