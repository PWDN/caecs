package com.vitaliirohozhyn_arsenisialitski.caecs.utils;

public enum MaterialType {
    GOLD( // https://en.wikipedia.org/wiki/Gold
            "Gold",
            2.49, // https://en.wikipedia.org/wiki/Table_of_specific_heat_capacities
            318.0, // https://en.wikipedia.org/wiki/List_of_thermal_conductivities
            0.99949, // https://www.mit.edu/~6.777/matprops/gold.htm
            0.0, // logicznie chyba
            19280.0, // https://www.mit.edu/~6.777/matprops/gold.htm
            false,
            MaterialState.SOLID,
            1337,
            3129,
            5000
            ),
    VACUUM(
            "Vacuum",
            0.0,
            0.0,
            1.0,
            1.0,
            0.0,
            false,
            MaterialState.VACUUM,
            10,
            11,
            9999), 
    COPPER(
            "Copper",
            3.45,
            401,
            0.9994,
            0.01,//test 
            8940,
            false,
            MaterialState.SOLID,
            1357,
            2835,
            4500),
    AIR(
            "Air",
            0.001297,
            0.025,
            1.00000037,
            1.00058986,
            1.204,
            false,
            MaterialState.GAS,
            15,         // z głowy
            20,        // z glowy
            7000       // z glowy
            ),
     WATER(
            "Water",
            4.138,
            0.5918,
            0.9999,  
            88,
            1,
            false,
            MaterialState.LIQUID,
            273,
            373,        //K = C + 273
            1000
            ),
      WALL(
           "Wall",
           0,
           0,
           0,
           0,
           1,
           false, 
           MaterialState.SOLID,
           99980,
           99990,
           99999
      );

    public final String name;
    public final Double volumetricHeatCapacity; // s(T) = J*(K^-1)*(cm^-3) //
                                                // https://en.wikipedia.org/wiki/Volumetric_heat_capacity
    public final Double thermalConductivity; // k = (W/(m*K)) // https://en.wikipedia.org/wiki/Thermal_conduction
    public final Double relativePermeability; // względna przenikalność magnetyczna (mu) //
                                              // https://en.wikipedia.org/wiki/Permeability_(electromagnetism)
    public final Double relativePermittivity; // względna przenikalność elektryczna (epsilon) //
                                              // https://en.wikipedia.org/wiki/Permittivity
    public final Double density; // ro = g/(m^3) // https://en.wikipedia.org/wiki/Density
    public final Boolean canBeSetOnFire;
    public final MaterialState defaultState;
    // public protected Integer usualState; // Zwykły stan materialu   1 - Solid(wall) - 2 - Solid(powder) - 3 - Liquid 4 - Vapor 5 - Plasm (fire)     
     public final Double meltingPoint;    // Temperatura topnięcia
     public final Double boillingPoint;   // Temperatura wrzenia
     public final Double selfSparkPoint;  // Temperatura samozapalenia 


    private MaterialType(
            String a_n,
            double a_vhc,
            double a_tc,
            double a_mu,
            double a_epsilon,
            double a_d,
            boolean a_cbsof,
            MaterialState a_state,
            double a_tmelt,
            double a_tboill,
            double a_tspark
            ) {
        this.name = a_n;
        this.volumetricHeatCapacity = a_vhc;
        this.thermalConductivity = a_tc;
        this.relativePermeability = a_mu;
        this.relativePermittivity = a_epsilon;
        this.density = a_d;
        this.canBeSetOnFire = a_cbsof;
        this.defaultState = a_state;
        this.meltingPoint = a_tmelt;
        this.boillingPoint = a_tboill;
        this.selfSparkPoint = a_tspark;
    }
}
