package com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems;

import java.awt.Graphics;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.AirComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ColorComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.graphics.Viewport;

public class RenderSystem extends ECSSystem {
    private final Viewport viewport;
    private final int zoom;

    public RenderSystem(ECS a_ecs, int a_zoom) {
        super(a_ecs);
        this.viewport = new Viewport();
        this.zoom = a_zoom;
    }

    public Viewport getViewport() {
        return this.viewport;
    }

    public void clearViewPort() {
        this.viewport.getGraphicsReady().clearRect(0, 0, 400, 400);
    }

    public void onFrameEnd(Entity a_entity) {
        if (a_entity.doesEntityHasComponentOfType(AirComponent.class))
            return;
        if (!a_entity.doesEntityHasComponentOfType(PositionComponent.class)
                || !a_entity.doesEntityHasComponentOfType(ColorComponent.class))
            return;
        ColorComponent comp = (ColorComponent) a_entity.getFirstComponentOfType(ColorComponent.class);
        PositionComponent position = (PositionComponent) a_entity.getFirstComponentOfType(PositionComponent.class);
        Graphics g = this.viewport.getGraphicsReady();
        g.setColor(comp.color);
        g.fillRect(
                position.x * this.zoom,
                position.y * this.zoom,
                this.zoom,
                this.zoom);
    }
}
