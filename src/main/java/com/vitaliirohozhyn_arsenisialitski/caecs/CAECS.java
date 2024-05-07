package com.vitaliirohozhyn_arsenisialitski.caecs;
import javax.swing.*;
import java.lang.Thread;
import java.util.Random;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.AirComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ColorComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.IronComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.SandComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

class Viewport extends Canvas {
	public Viewport() {
		super();
		createBufferStrategy(1);
	}
	public void draw() {}
  public Graphics getGraphicsReady() {
  	return this.getBufferStrategy().getDrawGraphics();
  }
}

class RenderSystem extends ECSSystem {
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
		if (a_entity.doesEntityHasComponentOfType(AirComponent.class)) return;
		if (!a_entity.doesEntityHasComponentOfType(PositionComponent.class) || !a_entity.doesEntityHasComponentOfType(ColorComponent.class)) return;
		ColorComponent comp = (ColorComponent)a_entity.getFirstComponentOfType(ColorComponent.class);
		PositionComponent position = (PositionComponent)a_entity.getFirstComponentOfType(PositionComponent.class);
		Graphics g = this.viewport.getGraphicsReady();
		g.setColor(comp.color);
		g.fillRect(
			position.x * this.zoom, 
			position.y * this.zoom, 
			this.zoom, 
			this.zoom
		);
	}
}

class PhysicsSystem extends ECSSystem {
	public PhysicsSystem(ECS a_ecs) {
		super(a_ecs);
	};
	public void onFrameStart(Entity a_entity) {
		if (!a_entity.doesEntityHasComponentOfType(SandComponent.class)) return;
		PositionComponent position = (PositionComponent)a_entity.getFirstComponentOfType(PositionComponent.class);
		if(this.ecs.findFirstEntityByFilter(
			(a_entity_in) -> {
				PositionComponent position_in = (PositionComponent)a_entity_in.getFirstComponentOfType(PositionComponent.class);
				return (position_in.y == position.y + 1 && position_in.x == position.x);
			}
		) == null) {
			position.y += 1;
		} else {
			Random rand = new Random();
			boolean side = rand.nextBoolean();
			int cast = side ? 1 : -1;
			if (this.ecs.findFirstEntityByFilter(
				(a_entity_in) -> {
					PositionComponent position_in = (PositionComponent)a_entity_in.getFirstComponentOfType(
						PositionComponent.class);
					return (position_in.y == position.y + 1 && position_in.x == position.x + cast);
				}
			) == null) {
				position.y += 1;
				position.x = position.x + cast;
			}
		}
	}
}

public class CAECS {
	public static void main(String[] args) {
		ECS ecs = new ECS();
		for (int i = 1; i <= 5; i++) {
			ecs.addEntity(
				new Entity(
					new Component[]{
						new SandComponent(),
						new PositionComponent(1+i, 4 + i),
						new ColorComponent(new Color(150, 100, 200))
					}
				)
			);
		}
		for (int i = 1; i <= 4; i++) {
			ecs.addEntity(
				new Entity(
					new Component[]{
						new SandComponent(),
						new PositionComponent(3, 1 + i),
						new ColorComponent(new Color(150, 100, 200))
					}
				)
			);
		}
		for (int i  = 1; i <= 11; i++) {
			ecs.addEntity(
				new Entity(
					new Component[]{
						new IronComponent(),
						new PositionComponent(i, 14),
						new ColorComponent(new Color(150, 150, 150))
					}
				)
			);
		}
		RenderSystem render = new RenderSystem(ecs, 20);
		ecs.registerSystem(render);
		PhysicsSystem phys = new PhysicsSystem(ecs);
		ecs.registerSystem(phys);
		JFrame f=new JFrame();  
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(400, 400);
    f.add(render.getViewport());	
		f.setVisible(true);

		while(true) {
			render.clearViewPort();
			ecs.run();
			// System.out.println("====");
			try{
				Thread.sleep(200);
			} catch (Exception e) {
			};
		}
	}
}

