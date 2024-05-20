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

public class Viewport extends JPanel {
    public Consumer<Point> useOnClick;
    private final ECS ecs;

    public Viewport(ECS a_ecs) {
        super();
        this.ecs = a_ecs;
        this.useOnClick = null;
        this.setSize(400, 400);
        this.setPreferredSize(new Dimension(400, 400));
        this.setMinimumSize(new Dimension(400, 400));
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if (useOnClick == null)
                    return;
                System.out.println(e.getPoint());
                System.out.println(useOnClick);
                Double newX = (double) e.getPoint().x / 20;
                Double newY = (double) e.getPoint().y / 20;
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
                System.out.println(e.getPoint());
                System.out.println(useOnClick);
                Double newX = (double) e.getPoint().x / 20;
                Double newY = (double) e.getPoint().y / 20;
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
            Color clrn = new Color(clr.getRed(), clr.getBlue() + chrg.charge, clr.getGreen());
            g.setColor(clrn);
            g.fillRect(
                    position.x * 20,
                    position.y * 20,
                    20,
                    20);
        }
    }

    // public void draw() {
    // }

    // public Graphics getGraphicsReady() {
    // return this.getBufferStrategy().getDrawGraphics();
    // }
}
