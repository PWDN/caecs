package com.vitaliirohozhyn_arsenisialitski.caecs.ecs;

public abstract class ECSSystem {
    protected final ECS ecs;

    public ECSSystem(ECS a_ecs) {
        this.ecs = a_ecs;
    }

    public void onFrameStart(final Entity a_entity) {
    }

    public void onFrameEnd(final Entity a_entity) {
    }

    public void onFrameStartBatched(final Entity a_entity[]) {
    }

    public void onFrameEndBatched(final Entity a_entiy[]) {
    }
}
