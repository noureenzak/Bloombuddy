package com.bloombuddy.game.patterns;

import com.bloombuddy.game.models.items.Item;
import com.bloombuddy.game.models.items.ItemType;
import javafx.scene.canvas.GraphicsContext;

public class ItemFactory {
    public static Item createItem(GraphicsContext gc, double x, double y, ItemType type) {
        Item item = new Item(gc, x, y, type);
        System.out.println("Created item of type: " + type);
        return item;
    }
}
