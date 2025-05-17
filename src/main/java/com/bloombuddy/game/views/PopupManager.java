package com.bloombuddy.game.views;


import com.bloombuddy.game.util.AudioManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.ImageCursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * PopupManager is responsible for showing all popup dialogs in the game.
 * These include the rules at the start, facts after interactions, and result screens.
 */
public class PopupManager {

    // Cursor image used in all popup scenes
    private static final Image CURSOR = new Image(PopupManager.class.getResource("/assets/images/cursor.png").toExternalForm());

    // Shows a simple popup at game start explaining the rules
    public static void showRulesPopup() {
        showPopup(
            "Drag healthy elements (water, sun, air, fertilizer) to help your plant grow.\nAvoid harmful items. You have 6 turns.",
            "/assets/images/popup.png"
        );
    }

    // Shows a popup containing a fun scientific fact about an element
    public static void showScientificFact(String factText) {
        showPopup(factText, "/assets/images/ScientificFactBackground.png");
    }

    // Helper to show a generic popup with a message and background
    private static void showPopup(String message, String backgroundImagePath) {
        Platform.runLater(() -> {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL); // Blocks input to other windows
            popup.initStyle(StageStyle.TRANSPARENT);        // No title bar
            popup.setResizable(false);

            // Background image
            ImageView bg = new ImageView(new Image(PopupManager.class.getResource(backgroundImagePath).toExternalForm()));
            bg.setFitWidth(500);
            bg.setFitHeight(300);

            // Message label
            Label text = new Label(message);
            text.setFont(new Font("Comic Sans MS", 16));
            text.setStyle("-fx-text-fill: black;");
            text.setWrapText(true);
            text.setMaxWidth(300);

            // OK button + message arranged vertically
            VBox content = new VBox(10, text, createOkButton(popup::close));
            content.setAlignment(Pos.CENTER);

            StackPane layout = new StackPane(bg, content);
            layout.setStyle("-fx-background-color: transparent;");

            Scene scene = new Scene(layout, 500, 300);
            scene.setFill(null); // Transparent scene
            scene.setCursor(new ImageCursor(CURSOR));

            popup.setScene(scene);
            popup.show();  // Show popup without blocking the main app
        });
    }

    // Shows the game over screen: won, passed, or lost
    public static void showGameEnd(String resultType, Runnable onRetry, Runnable onExit) {
        Platform.runLater(() -> {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initStyle(StageStyle.TRANSPARENT);
            popup.setResizable(false);

            // Choose image based on result
            String imagePath = switch (resultType) {
                case "won" -> "/assets/images/won.png";
                case "passed" -> "/assets/images/pass.png";
                case "lost" -> "/assets/images/lost.png";
                default -> throw new IllegalArgumentException("Invalid result type");
            };

            // Play corresponding audio
            if (resultType.equals("won") || resultType.equals("passed")) {
                AudioManager.playWinResult();
            } else {
                AudioManager.playPlantDied();
            }

            // Result image
            ImageView resultImg = new ImageView(new Image(PopupManager.class.getResource(imagePath).toExternalForm()));
            resultImg.setFitWidth(800);
            resultImg.setPreserveRatio(true);

            // Retry button
            ImageView retryBtn = new ImageView(new Image(PopupManager.class.getResource("/assets/images/replay.png").toExternalForm()));
            retryBtn.setFitWidth(300);
            retryBtn.setPreserveRatio(true);
            retryBtn.setOnMouseEntered(e -> retryBtn.setTranslateY(-3)); // Hover effect
            retryBtn.setOnMouseExited(e -> retryBtn.setTranslateY(0));
            retryBtn.setOnMouseClicked(e -> {
                popup.close();
                onRetry.run(); // Restart game
            });

            // Exit button
            ImageView exitBtn = new ImageView(new Image(PopupManager.class.getResource("/assets/images/exit.png").toExternalForm()));
            exitBtn.setFitWidth(300);
            exitBtn.setPreserveRatio(true);
            exitBtn.setOnMouseEntered(e -> exitBtn.setTranslateY(-3));
            exitBtn.setOnMouseExited(e -> exitBtn.setTranslateY(0));
            exitBtn.setOnMouseClicked(e -> {
                popup.close();
                onExit.run(); // Quit app
            });

            HBox buttons = new HBox(20, retryBtn, exitBtn);
            buttons.setAlignment(Pos.CENTER);

            VBox layout = new VBox(20, resultImg, buttons);
            layout.setAlignment(Pos.CENTER);
            layout.setStyle("-fx-background-color: transparent;");
            layout.setPadding(new Insets(60, 20, 20, 20));
            
            Scene scene = new Scene(layout, 900, 600); 
            scene.setFill(null);
            scene.setCursor(new ImageCursor(CURSOR));

            popup.setScene(scene);
            popup.show();
        });
    }

    // Creates the OK button used in most popups
    private static ImageView createOkButton(Runnable action) {
        Image okImage = new Image(PopupManager.class.getResource("/assets/images/ok.png").toExternalForm());
        ImageView okBtn = new ImageView(okImage);
        okBtn.setFitWidth(120);
        okBtn.setPreserveRatio(true);
        okBtn.setOnMouseEntered(e -> okBtn.setTranslateY(-3));
        okBtn.setOnMouseExited(e -> okBtn.setTranslateY(0));
        okBtn.setOnMouseClicked(e -> action.run());
        return okBtn;
    }
}
