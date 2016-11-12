package main.engine;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import main.MainController;
import main.game.SceneName;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Generic class representing basic scene. After constructor is called
 * new group is created which holds all the nodes for this particular
 * scene. Than method for creation of those nodes is called.
 */
public abstract class BaseScene extends Scene {

	private SceneName sceneName;
	protected MainController mainController;


	/**
	 * Indicates pane used for the scene
	 */
	protected BorderPane scenePane;

	/**
	 * Group holding all the nodes for this scene
	 */
	private Group nodes;

	protected BaseScene(MainController mainController, SceneName sceneName) {
		super(new Group(), GameEngine.WIDTH, GameEngine.HEIGHT);
		nodes = (Group) super.rootProperty().get();

		this.mainController = mainController;
		this.sceneName = sceneName;

		createSceneNodes();
	}

	protected void setBackground(String path) {
		ImageView imgView = null;
		try {
			imgView = loadImageView(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getNodes().getChildren().add(imgView);
	}

	/**
	 * Returns all nodes assigned to this scene
	 *
	 * @return all nodes in the scene
	 */
	protected Group getNodes() {
		return nodes;
	}

	/**
	 * This method is called upon instantiation. Shows the scene with
	 * animation defined in the subclass.
	 */
	protected void animateSceneIn() {
	}

	/**
	 * Subclasses need to override this method. All the nodes that
	 * are supposed to be displayed are created.
	 */
	protected abstract void createSceneNodes();

	protected void createPane() {
		scenePane = new BorderPane();
		scenePane.setPrefSize(GameEngine.WIDTH, GameEngine.HEIGHT);
		scenePane.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		scenePane.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		scenePane.setId("scenePane");

		getNodes().getChildren().addAll(scenePane);
	}

	public SceneName getSceneName() {
		return sceneName;
	}

	protected void addStylesheet(String stylesheetPath) {
		this.getStylesheets().add(stylesheetPath);
	}

	protected void addNodeToScene(Node node) {
		getNodes().getChildren().add(node);
	}

	public void showDialog(Alert.AlertType alertType, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.showAndWait();
	}

	private ImageView loadImageView(String path) throws IOException {
		InputStream is = Files.newInputStream(Paths.get(path));
		Image img = new Image(is);

		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(GameEngine.WIDTH);
		imgView.setFitHeight(GameEngine.HEIGHT);
		is.close();

		return imgView;
	}
}
