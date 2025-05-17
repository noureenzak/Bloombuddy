package com.bloombuddy.game.patterns;

import com.bloombuddy.game.models.Plant;
import com.bloombuddy.game.models.items.Item;
import com.bloombuddy.game.models.items.ItemType;
/**
 * Defines plant behavior when it's in a wilting state.
 * Good items offer smaller benefit; bad items do more damage.
 */
public class WiltingState implements PlantState {
    @Override
    public boolean applyEffect(Plant plant, Item item) {
        ItemType type = item.getType();
        switch (type) {
            case WATER, SUNLIGHT, CARBON_DIOXIDE, FERTILIZER -> {
                plant.changeHealth(+5);
                return true;
            }
            default -> {
                plant.changeHealth(-20);
                return false;
            }
        }
    }
}
