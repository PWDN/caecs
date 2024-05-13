package com.vitaliirohozhyn_arsenisialitski.caecs.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class Viewport extends Canvas {
    public Viewport() {
        super();
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
    }

    public void draw() {
    }

    public Graphics getGraphicsReady() {
        return this.getBufferStrategy().getDrawGraphics();
    }
}


