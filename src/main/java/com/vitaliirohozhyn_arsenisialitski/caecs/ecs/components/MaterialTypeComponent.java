package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;

import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialType;

public class MaterialTypeComponent implements Component {
    public MaterialType materialType;

    public MaterialTypeComponent(MaterialType a_materialType) {
        this.materialType = a_materialType;
    }

    public String toString() {
        StringBuilder build = new StringBuilder();
        build.append("Material type:");
        build.append(this.materialType.name);
        return build.toString();
    }

    public boolean IsConductivityZero(){
        if (this.materialType.conductivity == 0) return true;
        else{return false;}
    }

}
