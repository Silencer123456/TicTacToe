package main;

import main.engine.BaseScene;
import main.engine.GameEngine;
import main.engine.MultiplayerGame;
import main.engine.SceneManager;
import main.game.*;
import main.networking.ClientCommand;
import main.networking.TcpModel;

public class MainController {

	public Player player;

	private SceneManager sceneManager;
	private MultiplayerGame gameM;

	private GameEngine gameEngine;

	public MainController(GameEngine gameEngine, SceneManager sceneManager) {
		this.gameEngine = gameEngine;

		this.player = new Player();

		this.sceneManager = sceneManager;
		this.gameM = new MultiplayerGame(this);

		MenuScene menuScene = new MenuScene(this, SceneName.MENU);
		GameScene gameScene = new GameScene(this, SceneName.GAME);
		WaitingScene waitingScene = new WaitingScene(this, SceneName.WAITING);
		addScene(menuScene);
		addScene(gameScene);
		addScene(waitingScene);

		setActiveScene(gameScene.getSceneName());
	}

	/**
	 * Start single or multi
	 */
	public void newGame(String gameId, String boardSize) {
	    int size = Integer.parseInt(boardSize);

        GameScene gameScene = (GameScene) getSceneManager().getSceneByName(SceneName.GAME);
        gameScene.createBoard(gameId, size);
	}

	public void tilePressed(int x, int y) {
	    send(ClientCommand.MOVE.name(), x+"", y+"");
    }

    public void quitGame() {
        send(ClientCommand.GAME_QUIT.name());
        setActiveScene(SceneName.MENU);
    }

	public void send(String... tokens) {
		gameM.getTcpModel().sendMessage(tokens);
	}

	public void onApplicationClose() {
		gameM.getTcpModel().shutdown();
	}

	private SceneManager getSceneManager() {
		return sceneManager;
	}

	public BaseScene getScene(SceneName sceneName) {
		return getSceneManager().getSceneByName(sceneName);
	}

	public BaseScene getActiveScene() {
		return getSceneManager().getActiveScene();
	}

	public void setActiveScene(SceneName sceneName) {
		getSceneManager().setActiveScene(sceneName);
	}

	private void addScene(BaseScene baseScene) {
		getSceneManager().addScene(baseScene);
	}

	public TcpModel getGameClient() {
		return gameM.getTcpModel();
	}

	public String getGameTitle() {
		return gameEngine.getWindowTitle();
	}
}
