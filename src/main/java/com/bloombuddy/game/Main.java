package com.bloombuddy.game;

import com.bloombuddy.game.controllers.GameEngine;
import com.bloombuddy.game.views.SceneManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application entry point.
 * Launches the BloomBuddy game window.
 */
public class Main extends Application {
    private static Stage mainStage;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
     // Set the window to fill the screen
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());

        // Initialize game state
        GameEngine.getInstance().resetGame();

        // Load the start scene
        Scene startScene = SceneManager.createStartScene();
        stage.setScene(startScene);
        stage.setTitle("BloomBuddy");
        
        stage.show();
    }

    // Switch to the main gameplay scene
    public static void switchToGameScene() {
        Scene gameScene = SceneManager.createGameScene();
        mainStage.setScene(gameScene);
        mainStage.setMaximized(true);
    }

    // Switch back to the start menu scene
    public static void switchToStartScene() {
        Scene startScene = SceneManager.createStartScene();
        mainStage.setScene(startScene);
        mainStage.setMaximized(true);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
