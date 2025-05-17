package com.bloombuddy.game.patterns;

import com.bloombuddy.game.models.Plant;
import com.bloombuddy.game.models.items.Item;
import com.bloombuddy.game.models.items.ItemType;

public class HealthyState implements PlantState {
    @Override
    public boolean applyEffect(Plant plant, Item item) {
        ItemType type = item.getType();
        switch (type) {
            case WATER, SUNLIGHT, CARBON_DIOXIDE, FERTILIZER -> {
                plant.changeHealth(+15);
                return true;
            }
            default -> {
                plant.changeHealth(-15);
                return false;
            }
        }
    }
}
