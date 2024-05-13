package com.vitaliirohozhyn_arsenisialitski.caecs;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import java.lang.Thread;
import java.util.Random;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Component;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ColorComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.IronComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.SandComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems.KinematicsSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems.RenderSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.graphics.MainScreen;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECSSystem;

import java.awt.Color;

public class CAECS {
    public static void main(String[] args) {
        ECS ecs = new ECS();
        for (int i = 1; i <= 5; i++) {
            ecs.addEntity(
                    new Entity(
                            new Component[] {
                                    new SandComponent(),
                                    new PositionComponent(1 + i, 4 + i),
                                    new ColorComponent(new Color(150, 100, 200))
                            }));
        }
        for (int i = 1; i <= 4; i++) {
            ecs.addEntity(
                    new Entity(
                            new Component[] {
                                    new SandComponent(),
                                    new PositionComponent(3, 1 + i),
                                    new ColorComponent(new Color(150, 100, 200))
                            }));
        }
        for (int i = 1; i <= 11; i++) {
            ecs.addEntity(
                    new Entity(
                            new Component[] {
                                    new IronComponent(),
                                    new PositionComponent(i, 14),
                                    new ColorComponent(new Color(150, 150, 150))
                            }));
        }
        RenderSystem render = new RenderSystem(ecs, 20);
        ecs.registerSystem(render);
        KinematicsSystem phys = new KinematicsSystem(ecs);
        ecs.registerSystem(phys);
        try{
          // MetalLookAndFeel.setCurrentTheme(new OceanTheme());
          // MetalLookAndFeel.setCurrentTheme(new OceanTheme());
          // MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
          UIManager.setLookAndFeel(new MetalLookAndFeel()); 

          // UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
        MainScreen sc = new MainScreen();
        
        sc.setupViewport(render.getViewport());
        sc.showWindow();        
        while (true) {
            render.clearViewPort();
            ecs.run();
            // System.out.println("====");
            try {
                Thread.sleep(16);
            } catch (Exception e) {
            }
            ;
        }
    }
}
