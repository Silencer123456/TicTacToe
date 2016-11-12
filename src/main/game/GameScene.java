package main.game;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.MainController;
import main.engine.BaseScene;
import main.engine.GameEngine;

public class GameScene extends BaseScene {

    private static final String stylesheetPath = "file:resources/stylesheets/gameScene.css";

    protected Text gameIdText;
    public Text gameIdValue;

    private VBox topPane;
    private VBox leftPane;
    private VBox rightPane;
    private VBox bottomPane;

    private Text nameText;

    public GameScene(MainController mainController, SceneName sceneName) {
        super(mainController, sceneName);
        addStylesheet(stylesheetPath);

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                mainController.setActiveScene(SceneName.MENU);
            }
        });
    }

    @Override
    protected void createSceneNodes() {

        super.createPane();

        scenePane.setPrefSize(GameEngine.WIDTH, GameEngine.HEIGHT);

        topPane = createTopPane();
        scenePane.setTop(topPane);

        rightPane = createRightPane();
        scenePane.setRight(rightPane);

        leftPane = createLeftPane();
        scenePane.setLeft(leftPane);

        bottomPane = createBottomPane();
        scenePane.setBottom(bottomPane);

        createBoard("TEST", 20);
    }

    private VBox createTopPane() {

        VBox vBox = new VBox(10);
        vBox.setPrefHeight(80);

        HBox topHBox = new HBox(10);
        topHBox.setPrefSize(GameEngine.WIDTH, 30);
        gameIdText = new Text("MultiplayerGame ID: ");
        gameIdValue = new Text();
        nameText = new Text();

        topHBox.getChildren().addAll(gameIdText, gameIdValue, nameText);

        HBox hBox = new HBox(10);
        hBox.setPrefSize(GameEngine.WIDTH, 40);
        Button testButton = new Button("MENU");
        testButton.setOnMouseClicked(event -> mainController.setActiveScene(SceneName.MENU));

        Button testButton2 = new Button("TEST");
        Button testButton3 = new Button("TEST");
        Button testButton4 = new Button("TEST");
        Button testButton5 = new Button("TEST");
        Button testButton6 = new Button("TEST");
        hBox.getChildren().addAll(testButton, testButton2, testButton3, testButton4, testButton5, testButton6);

        hBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(topHBox, hBox);

        return vBox;
    }

    private VBox createLeftPane() {
        VBox leftBox = new VBox(10);
        leftBox.setPrefWidth(150);

        return leftBox;
    }

    private VBox createRightPane() {
        VBox rightBox = new VBox(10);
        rightBox.setPrefWidth(150);

        return rightBox;
    }

    public void createBoard(String gameId, int boardSize) {
        gameIdValue.setText(gameId);

        int width = (int) (GameEngine.WIDTH - leftPane.getPrefWidth() - rightPane.getPrefWidth());
        int height = (int) (GameEngine.HEIGHT - topPane.getPrefHeight() - bottomPane.getPrefHeight());
        Board board = new Board(mainController, boardSize, Math.min(width, height));

        scenePane.setCenter(board);

    }

    private VBox createBottomPane() {
        VBox bottomBox = new VBox(10);
        bottomBox.setPrefHeight(40);

        return bottomBox;
    }

    @Override
    public void animateSceneIn() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), getNodes());
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(getNodes().getOpacity());
        fadeTransition.play();
    }
}
