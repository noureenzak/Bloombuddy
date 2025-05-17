package com.bloombuddy.game.patterns;

import com.bloombuddy.game.views.GameController;
import javafx.scene.Scene;

public class GameSceneStrategy implements SceneStrategy {
    @Override
    public Scene buildScene() {
        return new GameController().createGameScene();
    }
}
