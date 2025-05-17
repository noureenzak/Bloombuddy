package com.bloombuddy.game.patterns;

import javafx.scene.Scene;
/**
 * Strategy interface for building different scenes in the game.
 * Implementations: StartSceneStrategy, GameSceneStrategy
 */
public interface SceneStrategy {
    Scene buildScene();
}
