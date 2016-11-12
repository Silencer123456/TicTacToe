package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.engine.GameEngine;
import main.engine.SceneManager;
import main.game.TictactoeEngine;

public class Main extends Application {

    private MainController mainController;
    private GameEngine tictactoeEngine = new TictactoeEngine(60, "TIC-TAC-TOE");

    @Override
    public void start(Stage primaryStage) throws Exception{
        Thread.setDefaultUncaughtExceptionHandler(Main::showError);

        tictactoeEngine.initialize(primaryStage);
        tictactoeEngine.beginGameLoop();

	    SceneManager sceneManager = new SceneManager(primaryStage);
        mainController = new MainController(tictactoeEngine, sceneManager);

        primaryStage.show();
    }

	@Override
	public void stop(){
		mainController.onApplicationClose();
		// Save file
	}

    private static void showError(Thread t, Throwable e) {
        System.err.println("***Default exception handler***");
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
            System.err.println("An unexpected error occurred in " + t);
            e.printStackTrace();
        }
    }

    private static void showErrorDialog(Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application error");
        alert.setHeaderText(e.getMessage());
        alert.setContentText(e.getMessage());
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
