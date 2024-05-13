package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

public enum PhysicsConstant {
  FREEFALLACCELERATION(9.8), // m/s^2
  GRAVITATIONAL(6.67430 * Math.pow(10, -11)); // N * m^2 * kg^-2 

  public final Double value;

  private PhysicsConstant(Double a_value) {
    this.value = a_value;
  }
}
