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
        MaterialTypeComponent own_sigma = a_entity.getFirstComponentOfType(MaterialTypeComponent.class);

        int x_pos = pos.x;
        int y_pos = pos.y;

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

        for(Entity i: neighbours) {
            ChargeComponent charge_in = i.getFirstComponentOfType(ChargeComponent.class);
            MaterialTypeComponent sigma_in = i.getFirstComponentOfType(MaterialTypeComponent.class);
            double gradient_sigma = (sigma_in.materialType.conductivity + own_sigma.materialType.conductivity)*
                                    (sigma_in.IsConductivityZero() ? 0:1)*
                                    (own_sigma.IsConductivityZero() ? 0:1)/2;
            
            if(gradient_sigma != 0) {
            charge_in.charge += (charge.charge - charge_in.charge) / gradient_sigma;
            charge.charge -= (charge.charge - charge_in.charge) / gradient_sigma;
            }
            else{break;}

        }
    }
    // public void onFrameEndBatched(final ArrayList<Entity> a_entity) {
        // System.out.println(a_entity.size());
    // }
}
