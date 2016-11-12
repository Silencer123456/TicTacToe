package main.game;

import javafx.stage.Stage;
import main.engine.GameEngine;

public class TictactoeEngine extends GameEngine {

	public TictactoeEngine(int fps, String title) {
		super(fps, title);
	}

	@Override
	public void initialize(Stage pStage) {
		pStage.setTitle(getWindowTitle());
		pStage.setResizable(false);
		pStage.sizeToScene();
	}

	/*private void initGame(String gameId, String boardSizeStr) {
		int boardSize = Integer.parseInt(boardSizeStr);
		getSceneManager().setActiveScene(SceneName.GAME);
		piskvorkyGameHandler.initGame(gameId, boardSize);
	}*/
}
