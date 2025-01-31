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
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.PositionComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.ecs.components.TemperatureComponent;
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.Utils;

import com.vitaliirohozhyn_arsenisialitski.caecs.utils.UIAndSimulationSettings;

/**
 * Panel, w którym są rysowane wszystkie {@link Entity}
 */
public class Viewport extends JPanel {
    public Consumer<Point> useOnClick;
    private final ECS ecs; // Defaut:
    private final int game_size = 400; // 400
    private final int pixel_size = 20; // 20
    private final double over_charge_down = 100.0;
    private final double over_charge_up = 200.0;
    private final UIAndSimulationSettings settings;

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
                useOnClick.accept(newP);
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

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
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
            Color speedColor;
            Color clrn;
            switch (settings.renderMode) {
                case THERMAL:
                    double temp = i.getFirstComponentOfType(TemperatureComponent.class).temperature;
                    int roundedTemp = (int) Math.round(temp - 300f);
                    speedColor = new Color(
                            Utils.clamp(8 * roundedTemp, 0, 255),
                            Utils.clamp(2 * roundedTemp, 0, 255),
                            Utils.clamp(2 * roundedTemp, 0, 255));
                    clrn = Utils.moveColorTowards(clr, Color.RED, speedColor);
                    g.setColor(clrn);
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
