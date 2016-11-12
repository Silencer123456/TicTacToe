package main.game;

import main.MainController;
import main.engine.BaseScene;

/**
 * This is a default scene to be displayed in case scene we were looking for
 * was not found. Prevents nullPointerExceptions
 */
public class EmptyScene extends BaseScene {

	public EmptyScene(MainController mainController, SceneName sceneName) {
		super(mainController, sceneName);
	}

	@Override
	protected void createSceneNodes() {

	}
}
