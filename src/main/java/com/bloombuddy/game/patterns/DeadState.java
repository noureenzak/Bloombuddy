package com.bloombuddy.game.patterns;

import com.bloombuddy.game.models.Plant;
import com.bloombuddy.game.models.items.Item;

public class DeadState implements PlantState {
    @Override
    public boolean applyEffect(Plant plant, Item item) {
        return false; // dead plant doesn't react
    }
}
