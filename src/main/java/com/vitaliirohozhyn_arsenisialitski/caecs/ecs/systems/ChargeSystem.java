package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ChargeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialType;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.Utils;

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
        
        Entity neightbour = ecs.findFirstEntityByFilter((a_entity_in) -> {
            if (a_entity_in.getFirstComponentOfType(MaterialTypeComponent.class).materialType == MaterialType.VACUUM) return false;
            PositionComponent pos_in = a_entity_in.getFirstComponentOfType(PositionComponent.class);
            return(
                (pos.x + 1 == pos_in.x && pos.y == pos_in.y) ||
                (pos.x - 1 == pos_in.x && pos.y == pos_in.y) ||
                (pos.y + 1 == pos_in.y && pos.x == pos_in.x) ||
                (pos.y - 1 == pos_in.y && pos.x == pos_in.x)
            );
        });
        if (neightbour == null) return;

        ChargeComponent neightbour_charge_obj = neightbour.getFirstComponentOfType(ChargeComponent.class);
        double neightbour_charge = neightbour_charge_obj.charge;

        if (neightbour_charge < local_charge ){
            neightbour_charge_obj.charge += (charge.charge/2);
            charge.charge /= 2;
        }
    }
}
