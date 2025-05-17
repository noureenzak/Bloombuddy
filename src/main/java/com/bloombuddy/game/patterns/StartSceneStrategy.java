package com.bloombuddy.game.patterns;

import com.bloombuddy.game.Main;
import com.bloombuddy.game.util.AudioManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class StartSceneStrategy implements SceneStrategy {

    @Override
    public Scene buildScene() {
        StackPane root = new StackPane();

        // Load and show looping GIF as background
        ImageView background = new ImageView(
            new Image(getClass().getResource("/assets/images/opening.gif").toExternalForm())
        );
        background.setPreserveRatio(false);
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());

        // Start button and title
        VBox content = new VBox(20);
        content.setAlignment(Pos.BOTTOM_CENTER);
        content.setPadding(new Insets(0, 0, 80, 0));

        ImageView startBtn = new ImageView(new Image(getClass().getResource("/assets/images/button.png").toExternalForm()));
        startBtn.setFitWidth(200);
        startBtn.setPreserveRatio(true);

        Label title = new Label("");
        title.setFont(new Font("Comic Sans MS", 36));

        startBtn.setOnMouseEntered(e -> startBtn.setTranslateY(-5));
        startBtn.setOnMouseExited(e -> startBtn.setTranslateY(0));
        startBtn.setOnMouseClicked(e -> {
            AudioManager.playClick();
            Main.switchToGameScene();
        });

        content.getChildren().addAll(title, startBtn);
        root.getChildren().addAll(background, content);

        return new Scene(root);
    }
}