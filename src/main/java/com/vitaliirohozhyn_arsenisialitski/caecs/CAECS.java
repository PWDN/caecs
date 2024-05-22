package com.vitaliirohozhyn_arsenisialitski.caecs;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems.ChargeSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.systems.KinematicsSystem;
import com.vitaliirohozhyn_arsenisialitski.caecs.graphics.MainScreen;
import com.vitaliirohozhyn_arsenisialitski.caecs.graphics.Viewport;


import java.awt.EventQueue;

public class CAECS {
    public static void main(String[] args) {
        ECS ecs = new ECS();
        ChargeSystem chrg = new ChargeSystem(ecs);
        ecs.registerSystem(chrg);
        KinematicsSystem phys = new KinematicsSystem(ecs);
        ecs.registerSystem(phys);
        try {
            // MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            // MetalLookAndFeel.setCurrentTheme(new OceanTheme());
            // MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());

            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Viewport view = new Viewport(ecs);
        EventQueue.invokeLater(() -> {
            MainScreen sc = new MainScreen(ecs);
            sc.setupViewport(view);
            sc.showWindow();
        });
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService gui = Executors.newSingleThreadScheduledExecutor();
        gui.scheduleAtFixedRate(() -> {
            view.repaint();
        }, 0, 16, TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
            long startTime = System.nanoTime();
            ecs.run();
            long endTime = System.nanoTime();
            System.out.println((endTime - startTime)/1000);
            System.out.println("End");
        }, 0, 33, TimeUnit.MILLISECONDS);
    }
}
