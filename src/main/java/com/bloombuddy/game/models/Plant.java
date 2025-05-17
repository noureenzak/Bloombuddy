package com.bloombuddy.game.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.bloombuddy.game.models.items.Item;
import com.bloombuddy.game.patterns.PlantState;
import com.bloombuddy.game.patterns.WiltingState;
import com.bloombuddy.game.patterns.DeadState;
import com.bloombuddy.game.patterns.HealthyState;
import com.bloombuddy.game.patterns.NeutralState;

public class Plant {

    private int growthPoints;
    private int stage; // from 0 (start) to 6 (fully grown)
    private int health;
    private ImageView imageView;
    private PlantState state = new HealthyState(); // starts in healthy state

    public Plant(ImageView imageView) {
        this.imageView = imageView;
        this.growthPoints = 0;
        this.stage = 0;
        this.health = 100; // start with full health
        updateImage();
    }

    // Call this to grow the plant to next stage
    public void grow() {
        if (stage < 6) {
            stage++;
            updateImage();
        }
    }

    // Choose the correct image for the plant stage
    private void updateImage() {
        String path = switch (stage) {
            case 0 -> "/assets/images/plant/stage1.gif";
            case 1 -> "/assets/images/plant/sprout.gif";
            case 2 -> "/assets/images/plant/babyleaves.gif";
            case 3 -> "/assets/images/plant/bigleaves.gif";
            case 4 -> "/assets/images/plant/buds.gif";
            case 5 -> "/assets/images/plant/oneflower.gif";
            case 6 -> "/assets/images/plant/bloom.gif";
            default -> "/assets/images/plant/stage1.gif";
        };
        imageView.setImage(new Image(getClass().getResource(path).toExternalForm()));
    }

    // This is unused if you're using applyItem, but kept for legacy logic
    public void feed() {
        growthPoints++;
        if (growthPoints == 1) {
            stage = 1;
        } else {
            updateStage();
        }
        updateImage();
    }

    // Updates stage manually based on growth points
    private void updateStage() {
        if (growthPoints >= 12) stage = 6;
        else if (growthPoints >= 10) stage = 5;
        else if (growthPoints >= 8) stage = 4;
        else if (growthPoints >= 6) stage = 3;
        else if (growthPoints >= 4) stage = 2;
        else if (growthPoints >= 2) stage = 1;
        else stage = 0;
    }

    // Makes the plant look dead
    public void setDead() {
        imageView.setImage(new Image(getClass().getResource("/assets/images/plant/dead.gif").toExternalForm()));
    }

    public int getStage() {
        return stage;
    }

    // Adds or subtracts health, then checks what state the plant should be in
    public void changeHealth(int amount) {
        this.health += amount;
        if (this.health > 100) this.health = 100;
        if (this.health < 0) this.health = 0;
        checkState();
    }

    // Based on health, set the correct state
    public void checkState() {
        if (health <= 0) {
            setState(new DeadState());
        } else if (health < 30) {
            setState(new WiltingState());
        } else if (health < 70) {
            setState(new NeutralState());
        } else {
            setState(new HealthyState());
        }
    }

    // Replace current state
    public void setState(PlantState state) {
        this.state = state;
    }

    // Handle item drop based on current state behavior
    public boolean applyItem(Item item) {
        return state.applyEffect(this, item);
    }
}
