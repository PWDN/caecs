package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.MaterialState;

public class MaterialStateComponent implements Component {
  public MaterialState materialState;

  public MaterialStateComponent(MaterialState a_state) {
    this.materialState = a_state;
  }
  public String toString() {
    StringBuilder build = new StringBuilder();
    build.append("Material state:");
    build.append(this.materialState);
    return build.toString();
  }

}
