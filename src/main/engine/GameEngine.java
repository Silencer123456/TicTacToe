package main.engine;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.game.Player;
import main.game.SceneName;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates game loop, handles animations, sprites, collision detection.
 */
public abstract class GameEngine {

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	private static Timeline gameLoop;

	private final int framesPerSecond;
	private final String windowTitle;

	private final SpriteManager spriteManager = new SpriteManager();

	public GameEngine(final int fps, final String title) {
		this.framesPerSecond = fps;
		this.windowTitle = title;

		buildAndSetGameLoop();
	}

	protected static Timeline getGameLoop() {
		return gameLoop;
	}

	protected void setGameLoop(Timeline gameLoop) {
		GameEngine.gameLoop = gameLoop;
	}

	public abstract void initialize(final Stage primaryStage);

	private void buildAndSetGameLoop() {
		final Duration oneFrameAmt = Duration.millis(1000 / getFramesPerSecond());
		final KeyFrame oneFrame = new KeyFrame(oneFrameAmt, event -> {
			updateSprites();
			checkCollisions();
			cleanupSprites();
		});

		Timeline timeline = new Timeline(oneFrame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		setGameLoop(timeline);
	}


	protected void updateSprites() {
		spriteManager.getAllSprites().forEach(this::handleUpdate);
	}

	protected void handleUpdate(Sprite sprite) {
	}

	protected void checkCollisions() {
		spriteManager.resetCollisionsToCheck();

		for (Sprite spriteA : spriteManager.getCollisionsToCheck()) {
			for (Sprite spriteB : spriteManager.getAllSprites()) {
				if (handleCollision(spriteA, spriteB)) {
					break;
				}
			}
		}
	}

	protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
		return false;
	}

	protected void cleanupSprites() {
		spriteManager.cleanupSprites();
	}

	public void beginGameLoop() {
		getGameLoop().play();
	}

	protected int getFramesPerSecond() {
		return framesPerSecond;
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	protected SpriteManager getSpriteManager() {
		return spriteManager;
	}

}
