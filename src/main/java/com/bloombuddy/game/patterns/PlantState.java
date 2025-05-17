package com.bloombuddy.game.patterns;

import com.bloombuddy.game.models.Plant;
import com.bloombuddy.game.models.items.Item;

// Interface for plant states
public interface PlantState {
    boolean applyEffect(Plant plant, Item item);
}
