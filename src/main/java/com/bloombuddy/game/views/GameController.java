package com.bloombuddy.game.views;

import com.bloombuddy.game.controllers.GameEngine;
import com.bloombuddy.game.models.Plant;
import com.bloombuddy.game.models.items.Item;
import com.bloombuddy.game.models.items.ItemType;
import com.bloombuddy.game.patterns.ItemFactory;
import com.bloombuddy.game.util.AudioManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.ImageCursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

//Manages the full gameplay screen, UI layout, interactions, and drag-and-drop logic.
public class GameController {

    private StackPane rootStackPane;
    private BorderPane layout;
    private Plant plant;
    private Label dayLabel;
    private Label growthLabel;
    private ImageView[] hearts = new ImageView[3];
    private Label messageLabel;
    private static final Image cursorImage = new Image(GameController.class.getResource("/assets/images/cursor.png").toExternalForm());
    
    // Builds the entire game scene, sets up UI and media
    public Scene createGameScene() {
        rootStackPane = new StackPane();
        layout = new BorderPane();

        layout.setTop(createTopBar());
        layout.setCenter(createCenterPane());
        layout.setBottom(createItemBar());

        rootStackPane.getChildren().add(layout);

        ImageView bgView = new ImageView(new Image(getClass().getResource("/assets/images/background.gif").toExternalForm()));
        bgView.setPreserveRatio(false);
        bgView.fitWidthProperty().bind(rootStackPane.widthProperty());
        bgView.fitHeightProperty().bind(rootStackPane.heightProperty());
        bgView.setMouseTransparent(true);
        rootStackPane.getChildren().add(0, bgView);


        setupDragTarget();

        Scene scene = new Scene(rootStackPane);
        scene.setCursor(new ImageCursor(cursorImage));

        Platform.runLater(PopupManager::showRulesPopup);
        return scene;
    }

    // Builds the top bar UI with heart icons and game status labels
    private VBox createTopBar() {
        VBox top = new VBox(10);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(10));

        HBox heartsBox = new HBox(10);
        heartsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < hearts.length; i++) {
            Image heartImg = new Image(getClass().getResource("/assets/images/Heart.png").toExternalForm());
            hearts[i] = new ImageView(heartImg);
            hearts[i].setFitWidth(40);
            hearts[i].setFitHeight(40);
            heartsBox.getChildren().add(hearts[i]);
        }

        dayLabel = new Label("📅 Day 1 of 5");
        growthLabel = new Label("Growth: 0/6");
        dayLabel.setFont(new Font("Comic Sans MS", 18));
        growthLabel.setFont(new Font("Comic Sans MS", 18));
        top.getChildren().addAll(heartsBox, dayLabel, growthLabel);
        return top;
    }

    // Builds the center pane with plant and message box
    private StackPane createCenterPane() {
        StackPane center = new StackPane();
        center.setAlignment(Pos.CENTER);

        ImageView plantImg = new ImageView();
        plantImg.setFitWidth(450);
        plantImg.setFitHeight(450);
        plant = new Plant(plantImg);

        ImageView msgBox = new ImageView(new Image(getClass().getResource("/assets/images/message.png").toExternalForm()));
        msgBox.setFitWidth(550);
        msgBox.setFitHeight(280);

        messageLabel = new Label("");
        messageLabel.setFont(new Font("Comic Sans MS", 18));
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);
        messageLabel.setAlignment(Pos.CENTER);

        StackPane message = new StackPane(msgBox, messageLabel);
        StackPane.setAlignment(message, Pos.CENTER_RIGHT);
        StackPane.setMargin(message, new Insets(-300, 0, 100, 550));

        center.getChildren().addAll(plantImg, message);
        return center;
    }

    // Builds the bottom item bar with draggable items
    private HBox createItemBar() {
        HBox itemBar = new HBox(20);
        itemBar.setAlignment(Pos.CENTER);
        itemBar.setPadding(new Insets(20));
        for (ItemType type : ItemType.values()) {
            ImageView item = createDraggableItem(type);
            if (item != null) itemBar.getChildren().add(item);
        }
        return itemBar;
    }
    // Creates a draggable image for each item type (e.g., water, sun)
    private ImageView createDraggableItem(ItemType type) {
        String filename = switch (type) {
            case WATER -> "water.gif";
            case SUNLIGHT -> "sun.gif";
            case CARBON_DIOXIDE -> "air.gif";
            case FERTILIZER -> "fertilizer.gif";
            case CANDY -> "candy.gif";
            case TRASH -> "trash.gif";
            case SODA -> "soda.gif";
            case PIZZA -> "pizza.gif";
        };

        String path = "/assets/images/elements/" + filename;
        try {
            Image img = new Image(getClass().getResource(path).toExternalForm());
            ImageView view = new ImageView(img);
            view.setFitWidth(80);
            view.setFitHeight(80);
            view.setId(type.name());
            view.setOnDragDetected(e -> {
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(type.name());
                db.setContent(content);
                db.setDragView(view.snapshot(null, null));
                e.consume();
            });
            return view;
        } catch (Exception e) {
            System.err.println("Error loading item: " + path);
            return null;
        }
    }
    // Sets up the target area where items can be dropped (the game area)
    private void setupDragTarget() {
        rootStackPane.setOnDragOver(e -> {
            if (e.getGestureSource() != rootStackPane && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        rootStackPane.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasString()) {
                handleDrop(ItemType.valueOf(db.getString()));
                e.setDropCompleted(true);
            } else {
                e.setDropCompleted(false);
            }
            e.consume();
        });
    }
    // Called when an item is dropped on the plant; applies effects and updates state
    private void handleDrop(ItemType type) {
        GameEngine engine = GameEngine.getInstance();
        if (plant.getStage() >= 6 || engine.getHearts() == 0 || engine.getTurn() >= 6) return;

        Item item = ItemFactory.createItem(null, 0, 0, type);
        boolean isGood = plant.applyItem(item);

        if (isGood) {
            AudioManager.playCorrectItem();
            plant.grow();
            showMessage(getMessage(type));
            PopupManager.showScientificFact(getFact(type));
        } else {
            engine.loseHeart();
            AudioManager.playWrongItem();
            showMessage("That hurt the plant!");
            updateHearts();
        }

        engine.nextTurn();
        updateTopBar();

        if (engine.getHearts() == 0) {
            AudioManager.playPlantDied();
            plant.setDead();
            Platform.runLater(() -> pauseBeforeResult("lost"));
        } else if (plant.getStage() >= 6) {
            Platform.runLater(() -> pauseBeforeResult("won"));
        } else if (engine.getTurn() >= 6) {
            Platform.runLater(() -> pauseBeforeResult("passed"));
        }
    }
    
    // Delays the transition to the result screen for dramatic effect
    private void pauseBeforeResult(String resultType) {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> SceneManager.showResultScreen(resultType));
        pause.play();
    }
    // Visually updates hearts based on current lives
    private void updateHearts() {
        GameEngine engine = GameEngine.getInstance();
        for (int i = 0; i < hearts.length; i++) {
            hearts[i].setOpacity(i < engine.getHearts() ? 1.0 : 0.2);
        }
    }
    // Updates the top UI bar with current game status
    private void updateTopBar() {
        GameEngine engine = GameEngine.getInstance();
        dayLabel.setText("📅 Day " + engine.getDay() + " of 5");
        growthLabel.setText("Growth: " + plant.getStage() + "/6");
        if (engine.getTurn() % 2 == 0) showMessage(getStory(engine.getDay()));
    }

    private void showMessage(String msg) {
        messageLabel.setText(msg);
    }
    // Personalized messages shown after each correct item
    private String getMessage(ItemType type) {
        return switch (type) {
            case WATER -> "Yum! I was so thirsty. Can you now help me get some fresh air?";
            case SUNLIGHT -> "Ahh, warm sun! Now I think I need some water, please!";
            case CARBON_DIOXIDE -> "Thanks! I can breathe better now. Could you add some fertilizer next?";
            case FERTILIZER -> "That was tasty for my roots! Maybe some sunlight next?";
            default -> "Thanks for helping me!";
        };
    }
    // Educational popups shown for each helpful item
    private String getFact(ItemType type) {
        return switch (type) {
            case WATER -> "Water helps plants drink from the ground and stay strong.";
            case SUNLIGHT -> "Plants use sunlight to make food in their leaves. It's called photosynthesis.";
            case CARBON_DIOXIDE -> "Plants breathe in carbon dioxide from the air and give out oxygen.";
            case FERTILIZER -> "Fertilizer gives plants special nutrients like nitrogen to help them grow.";
            default -> "";
        };
    }
    // Flavor text based on the current day of the game
    private String getStory(int day) {
        return switch (day) {
            case 1 -> "A sprout is emerging! 🌱";
            case 2 -> "Soaking up sunlight!";
            case 3 -> "Feeling thirsty!";
            case 4 -> "Buds forming!";
            case 5 -> "Bloom time!";
            default -> "Keep growing!";
        };
    }
}
