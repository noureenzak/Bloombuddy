package com.bloombuddy.game.models.items;

import com.bloombuddy.game.core.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Item Class
 * Represents draggable items (water, sun, trash, etc.)
 */
public class Item extends GameObject {
    private ItemType type;

    public Item(GraphicsContext gc, double x, double y, ItemType type) {
        super(gc, x, y);
        this.type = type;
        loadImage();
    }

    /**
     * Load the correct image based on the item type.
     */
    private void loadImage() {
        switch (type) {
            case WATER -> img = new Image("file:assets/images/elements/water.gif");
            case SUNLIGHT -> img = new Image("file:assets/images/elements/sun.gif");
            case CARBON_DIOXIDE -> img = new Image("file:assets/images/elements/air.gif");
            case FERTILIZER -> img = new Image("file:assets/images/elements/fertilizer.gif");
            case CANDY -> img = new Image("file:assets/images/elements/candy.gif");
            case TRASH -> img = new Image("file:assets/images/elements/trash.gif");
            case SODA -> img = new Image("file:assets/images/elements/soda.gif");
            case PIZZA -> img = new Image("file:assets/images/elements/pizza.gif");
        }
    }

    public ItemType getType() {
        return type;
    }
}
