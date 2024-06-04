package com.vitaliirohozhyn_arsenisialitski.caecs.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;

import javax.swing.JPanel;

import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.ECS;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.Entity;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ChargeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.ColorComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.MaterialTypeComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.TemperatureComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.Utils;

import com.vitaliirohozhyn_arsenisialitski.caecs.utils.UIAndSimulationSettings;

public class Viewport extends JPanel {
    public Consumer<Point> useOnClick;
    private final ECS ecs; // Defaut:
    private final int game_size = 400; // 400
    private final int pixel_size = 20; // 20
    private final double over_charge_down = 100.0;
    private final double over_charge_up = 200.0;
    private final UIAndSimulationSettings settings;

    //private final int alfa = 255;

    public Viewport(ECS a_ecs) {
        super();
        this.ecs = a_ecs;
        this.useOnClick = null;
        this.setSize(game_size, game_size);
        this.setPreferredSize(new Dimension(game_size, game_size));
        this.setMinimumSize(new Dimension(game_size, game_size));
        this.settings = ecs.settings;
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {

                if (useOnClick == null)
                    return;
                Double newX = (double) e.getPoint().x / pixel_size;
                Double newY = (double) e.getPoint().y / pixel_size;
                Point newP = new Point((int) Math.floor(newX), (int) Math.floor(newY));
                // new Thread(() -> {
                useOnClick.accept(newP);
                // }).start();
            }

            public void mouseMoved(MouseEvent e) {
            }
        });
        this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (useOnClick == null)
                    return;
                Double newX = (double) e.getPoint().x / pixel_size;
                Double newY = (double) e.getPoint().y / pixel_size;
                Point newP = new Point((int) Math.floor(newX), (int) Math.floor(newY));
                useOnClick.accept(newP);
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        for (Entity i : this.ecs.getEntityList()) {
            if (!i.doesEntityHasComponentOfType(PositionComponent.class)
                    || !i.doesEntityHasComponentOfType(ColorComponent.class))
                return;
            PositionComponent position = (PositionComponent) i.getFirstComponentOfType(PositionComponent.class);
            ColorComponent comp = (ColorComponent) i.getFirstComponentOfType(ColorComponent.class);
            Color clr = comp.color;
            Color finalColor = new Color(ABORT);
            /*
             * int newRed = Utils.clamp((int) (clr.getRed() - (2 *
             * Math.round(chrg.charge))), 22, 255);
             * int newBlue = Utils.clamp((int) (clr.getBlue() + (10 *
             * Math.round(chrg.charge))), 0, 195);
             * int newGreen = Utils.clamp((int) (clr.getGreen() + (2 *
             * Math.round(chrg.charge))), 0, 255);
             */
            Color speedColor;
            Color clrn;

            switch (settings.renderMode) {
                case THERMAL:

                
                    int temp = i.getFirstComponentOfType(TemperatureComponent.class).temperature;

                    if ((temp >= 0) && (temp < 50)) {
                        finalColor = new Color(0, 0, 0);
                    } else if ((temp >= 50) && (temp < 100)) {
                        finalColor = new Color(2, 6,106);
                    } else if ((temp >= 100) && (temp < 150)) {
                        finalColor = new Color(12,17,172);
                    } else if ((temp >= 150) && (temp < 300)) {
                        finalColor = new Color(255,0,215);
                    }  else if ((temp >= 300) && (temp < 500)) {
                        finalColor = new Color(255,0,0);
                    }  else if ((temp >= 500) && (temp < 700)) {
                        finalColor = new Color(255,149,0);
                    }   else if ((temp >= 700) && (temp < 825)){
                        finalColor = new Color(232,255,0);
                    }   else if ((temp >= 825)){
                        finalColor = new Color(255,255,255);
                    }
                   
                //    speedColor = new Color(
                //         Utils.clamp(2 * (int)Math.log(1+temp), 0, 255),
                //         Utils.clamp(2 * (int)Math.log(1+temp), 0, 255),
                //         Utils.clamp(8 * (int)Math.log(1+temp), 0, 255));
                //     Color clrn = Utils.moveColorTowards(clr, finalColor, speedColor);
                //     //g.setColor(clrn);

                    g.setColor(finalColor);
                    g.fillRect(
                            position.x * pixel_size,
                            position.y * pixel_size,
                            pixel_size,
                            pixel_size);
                    break;
                case ELECTRICAL:

                    Double chrg = i.getFirstComponentOfType(ChargeComponent.class).charge;
                    if (chrg >= 0.0) {
                        finalColor = new Color(22, 195, 252);
                    } else if (chrg <= 0.0) {
                        finalColor = new Color(188, 188, 188);
                    } else {
                        finalColor = clr;
                    }
                    int roundedChrg = (int) (Math.round(chrg));

                   speedColor = new Color(
                            Utils.clamp(2 * roundedChrg, 0, 255),
                            Utils.clamp(2 * roundedChrg, 0, 255),
                            Utils.clamp(8 * roundedChrg, 0, 255));
                    clrn = Utils.moveColorTowards(clr, finalColor, speedColor);
                    g.setColor(clrn);
                    g.fillRect(
                            position.x * pixel_size,
                            position.y * pixel_size,
                            pixel_size,
                            pixel_size);
                    if (chrg >= over_charge_down && chrg <= over_charge_up) {
                        // Color ovch = Utils.moveColorTowards(clr, finalColor, speedColor);
                        g.setColor(Color.WHITE);
                        g.fillRect(
                                (int) Math.round(
                                        position.x * pixel_size + pixel_size * ((over_charge_down) / chrg - 0.5)),
                                (int) Math.round(
                                        position.y * pixel_size + pixel_size * ((over_charge_down) / chrg - 0.5)),
                                (int) Math.round(pixel_size * ((chrg) - over_charge_down) / over_charge_down),
                                (int) Math.round(pixel_size * ((chrg) - over_charge_down) / over_charge_down));
                    } else if (chrg > over_charge_up) {
                        g.setColor(Color.WHITE);
                        g.fillRect(
                                position.x * pixel_size,
                                position.y * pixel_size,
                                pixel_size,
                                pixel_size);
                    }
                    break;
                default:
                    g.setColor(clr);
                    g.fillRect(
                            position.x * pixel_size,
                            position.y * pixel_size,
                            pixel_size,
                            pixel_size);
                    break;
            }

        }
    }

}
