package com.bloombuddy.game.views;

import com.bloombuddy.game.Main;
import com.bloombuddy.game.controllers.GameEngine;
import com.bloombuddy.game.patterns.GameSceneStrategy;
import com.bloombuddy.game.patterns.SceneStrategy;
import com.bloombuddy.game.patterns.StartSceneStrategy;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Manages scene transitions and result screen display in the game.
public class SceneManager {
    // Builds and returns the start/welcome scene using StartSceneStrategy
    public static Scene createStartScene() {
        SceneStrategy strategy = new StartSceneStrategy();
        return strategy.buildScene();
    }
    // Builds and returns the main game scene using GameSceneStrategy
    public static Scene createGameScene() {
        SceneStrategy strategy = new GameSceneStrategy();
        return strategy.buildScene();
    }
    // Displays the game-end popup and handles actions for "home" (previously it was replay) and "Exit"
    public static void showResultScreen(String resultType) {
        PopupManager.showGameEnd(
            resultType,
            () -> {
            	GameEngine.getInstance().resetGame();
            	Stage stage = Main.getMainStage();

            	// Reset screen to normal size before reapplying maximized scene
            	stage.setFullScreen(false);
            	stage.setMaximized(false);
            	stage.setWidth(1280); // baseline resolution
            	stage.setHeight(720);

            	stage.setScene(createStartScene());
            	stage.setMaximized(true);

            },
            () -> {
                Platform.exit();
            }
        );
    }
}
