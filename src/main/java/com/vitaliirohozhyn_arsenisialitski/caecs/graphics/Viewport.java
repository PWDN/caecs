package com.vitaliirohozhyn_arsenisialitski.caecs.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.Consumer;


public class Viewport extends Canvas {
    public Consumer<Point> useOnClick;
    public Viewport() {
        super();
        this.useOnClick = null;
        createBufferStrategy(1);
        this.setSize(400, 400);
        this.setPreferredSize(new Dimension(400, 400));
        this.setMinimumSize(new Dimension(400, 400));
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                System.out.println(e.getLocationOnScreen().toString());
            }
            public void mouseMoved(MouseEvent e) {
            }
        });
        this.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if(useOnClick == null) return;
                System.out.println(e.getPoint());
                System.out.println(useOnClick);
                Double newX = (double)e.getPoint().x / 20;
                Double newY = (double)e.getPoint().y / 20;
                Point newP = new Point((int)Math.round(newX), (int)Math.round(newY));
                useOnClick.accept(newP);
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}        	
        });
    }


    public void draw() {
    }

    public Graphics getGraphicsReady() {
        return this.getBufferStrategy().getDrawGraphics();
    }
}


