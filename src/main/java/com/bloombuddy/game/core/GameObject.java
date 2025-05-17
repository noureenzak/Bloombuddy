package com.bloombuddy.game.core;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.Image;

public class GameObject {
    protected Image img;
    protected double x, y;
    protected GraphicsContext gc;
    
    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public GameObject(GraphicsContext gc, double x, double y) {
        this.gc = gc;
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (img != null)
        	gc.drawImage(img, x, y, 500, 500);  
    }
}
