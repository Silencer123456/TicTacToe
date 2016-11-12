package main.game;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.MainController;
import main.engine.BaseScene;

public class WaitingScene extends BaseScene {

	public WaitingScene(MainController mainController, SceneName sceneName) {
		super(mainController, sceneName);
	}
	@Override
	protected void createSceneNodes() {
		setBackground("resources/images/Background.jpg");

		super.createPane();

		Text text = new Text("Waiting for other player to connect...");
		BorderPane.setAlignment(text, Pos.CENTER);

		Button backToMenuButton = new Button("Menu");
		backToMenuButton.setOnMouseClicked(event -> {
			FadeTransition ft = new FadeTransition(Duration.seconds(0.5));
			ft.setFromValue(1);
			ft.setToValue(0);
			ft.setOnFinished(event1 -> mainController.quitGame());
			ft.play();
		});

		scenePane.setCenter(text);
		scenePane.setTop(backToMenuButton);
	}

	@Override
	public void animateSceneIn() {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), getNodes());
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(getNodes().getOpacity());
		fadeTransition.play();
	}
}
