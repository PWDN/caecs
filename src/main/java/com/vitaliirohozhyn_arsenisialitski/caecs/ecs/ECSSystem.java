package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

/**
 * Klasa abstrakcyjna, która jest szablonem do tworzenia innych systemów, czyli
 * jednostek "logicznych", które są wykonywane na wszystkich {@link Entity}
 */
public abstract class ECSSystem {
    protected final ECS ecs;

    public ECSSystem(ECS a_ecs) {
        this.ecs = a_ecs;
    }

    /**
     * Metoda, która będzie wykonana dla każdego {@link Entity} każdy frame
     * 
     * @param a_entity obecnie obrabiany {@link Entity}
     */
    public void run(final Entity a_entity) {
    }
}
