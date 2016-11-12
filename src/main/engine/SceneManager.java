package main.engine;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.game.SceneName;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

	private List<BaseScene> scenes;

	private Stage primaryStage;
	private Scene gameSurface;
	private Group sceneNodes;

	private BaseScene currentlyActiveScene;

	public SceneManager(Stage primaryStage) {
		this.primaryStage = primaryStage;

		scenes = new ArrayList<>();
	}

	public void addScene(BaseScene sceneToAdd) {
		scenes.add(sceneToAdd);
	}

	public void setActiveScene(SceneName sceneName) {
		BaseScene activeScene = getSceneByName(sceneName);

		if (activeScene != null) {
			this.currentlyActiveScene = activeScene;
			setSceneNodes(activeScene.getNodes());
			setGameSurface(activeScene);

			primaryStage.setScene(getGameSurface());
			activeScene.animateSceneIn();
		}
	}

	public BaseScene getSceneByName(SceneName sceneName) {
		BaseScene sceneToReturn = null;
		for (BaseScene currentScene : scenes) {
			if (currentScene.getSceneName() == sceneName) {
				sceneToReturn = currentScene;
			}
		}

		return sceneToReturn;
	}

	private void setGameSurface(Scene gameSurface) {
		this.gameSurface = gameSurface;
	}
	private Scene getGameSurface() {
		return gameSurface;
	}

	public Group getSceneNodes() {
		return sceneNodes;
	}

	private void setSceneNodes(Group sceneNodes) {
		this.sceneNodes = sceneNodes;
	}

	public BaseScene getActiveScene() {
		return currentlyActiveScene;
	}
}
