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
import com.vitaliirohozhyn_arsenisialitski.caecs.utils.Utils;

public class Viewport extends JPanel {
    public Consumer<Point> useOnClick;
    private final ECS ecs;              //  Defaut:
    private final int game_size = 400;  //  400
    private final int pixel_size = 20;  //  20

    public Viewport(ECS a_ecs) {
        super();
        this.ecs = a_ecs;
        this.useOnClick = null;
        this.setSize(game_size, game_size);
        this.setPreferredSize(new Dimension(game_size, game_size));
        this.setMinimumSize(new Dimension(game_size, game_size));
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
            ColorComponent comp = (ColorComponent) i.getFirstComponentOfType(ColorComponent.class);
            PositionComponent position = (PositionComponent) i.getFirstComponentOfType(PositionComponent.class);
            Color clr = comp.color;
            ChargeComponent chrg = i.getFirstComponentOfType(ChargeComponent.class);
            int newRed = Utils.clamp((int)(clr.getRed() - (10 * Math.round(chrg.charge))), 0, 255);
            int newBlue = Utils.clamp((int)(clr.getBlue() + (10 * Math.round(chrg.charge))), 0, 255);
            int newGreen = Utils.clamp((int)(clr.getGreen() - (10 * Math.round(chrg.charge))), 0, 255);
            Color clrn = new Color(newRed, newGreen, newBlue);
            g.setColor(clrn);
            g.fillRect(
                    position.x * pixel_size,
                    position.y * pixel_size,
                    pixel_size,
                    pixel_size);
        }
    }

}
