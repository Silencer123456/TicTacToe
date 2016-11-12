package main.game;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.Constants;
import main.MainController;
import main.engine.BaseScene;
import main.engine.GameEngine;
import main.networking.ClientCommand;
import main.networking.NetworkStatus;
import main.utils.CustomTextField;
import main.utils.IntegerTextField;
import main.utils.NameTextField;

import java.util.List;

public class MenuScene extends BaseScene {

	private static final String stylesheetPath = "file:resources/stylesheets/menuScene.css";

	public Text networkInfoMessage;
	public ErrorText connectionErrorText;
	public MenuButton playButton;
	public CustomTextField nameTextField;

	private MenuButton exitButton;
	private Text connectionStatusText;
	private Text playerName;
	private CustomTextField ipTextField;
	private IntegerTextField portTextField;
	private Button connectButton;
	private Button interruptButton;
	private ListView<String> openGamesListView;

	public MenuScene(MainController mainController, SceneName sceneName) {
		super(mainController, sceneName);
		addStylesheet(stylesheetPath);

		updateConnectionStatus(mainController.getGameClient().getCurrentNetworkStatus());
	}

	@Override
	public void animateSceneIn() {
		/*FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), getNodes());
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(getNodes().getOpacity());
		fadeTransition.play();*/
	}

	@Override
	protected void createSceneNodes() {
		setBackground("resources/images/Background.jpg");

		/**
		 * Darken background
		 */
		Rectangle bg = new Rectangle(GameEngine.WIDTH, GameEngine.HEIGHT);
		bg.setFill(Color.GRAY);
		bg.setOpacity(0.4);
		getNodes().getChildren().addAll(bg);

		super.createPane();

		HBox titleBox = createTitlePane();
		scenePane.setTop(titleBox);

		VBox menuBox = createMenu();
		scenePane.setLeft(menuBox);

		GridPane bottomPane = createBottomPane();
		scenePane.setBottom(bottomPane);

		StackPane lobby = createLobby();
		scenePane.setCenter(lobby);

		BorderPane.setAlignment(lobby, Pos.CENTER_LEFT);
	}

	private HBox createTitlePane() {
		HBox titleBox = new HBox(10);
		titleBox.setPrefHeight(200);
		titleBox.setAlignment(Pos.CENTER);
		Text text = new Text(mainController.getGameTitle());
		text.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 60));
		text.setFill(Color.DARKSLATEGRAY);

		titleBox.getChildren().add(text);
		return titleBox;
	}

	private VBox createMenu() {
		VBox menu0 = new VBox(10);

		//menu0.setStyle("-fx-background-color: #CD5C5C;");
		menu0.setPadding(new Insets(30, 10, 30, 30));
		menu0.setPrefWidth(320);
		menu0.setAlignment(Pos.TOP_CENTER);

		playButton = new MenuButton("PLAY");
		playButton.setOnMouseClicked(event -> {
			FadeTransition ft = new FadeTransition(Duration.seconds(0.5));
			ft.setFromValue(1);
			ft.setToValue(0);
			ft.setOnFinished(event1 -> handlePlayButtonClicked());
			ft.play();
		});
		playButton.setDisable(true);

		exitButton = new MenuButton("EXIT");
		exitButton.setOnMouseClicked(event -> {
			FadeTransition ft = new FadeTransition(Duration.seconds(0.5));
			ft.setFromValue(1);
			ft.setToValue(0);
			ft.play();
		});

		menu0.getChildren().addAll(playButton, exitButton);

		return menu0;
	}

	private StackPane createLobby() {
		StackPane stackPane = new StackPane();
		stackPane.setMaxWidth(Control.USE_PREF_SIZE);
		stackPane.setPrefWidth(600);

		VBox vBox = new VBox(10);
		//vBox.setStyle("-fx-background-color: #CD5C5C;");
		HBox nameHBox = new HBox(10);
		nameHBox.setAlignment(Pos.CENTER);
		nameTextField = new NameTextField();
		nameTextField.setPromptText("Enter your name");

		nameHBox.getChildren().add(nameTextField);
		vBox.getChildren().add(nameHBox);

		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER);
		Label ipLabel = new Label("IP:");
		ipLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
		Label portLabel = new Label("PORT:");
		portLabel.setFont(Font.font(null, FontWeight.BOLD, 16));

		ipTextField = new NameTextField(Constants.instance().DEFAULT_HOST);
		portTextField = new IntegerTextField(Constants.instance().DEFAULT_PORT + "");
		connectButton = new Button("Connect");
		connectButton.setDefaultButton(true);

		connectButton.setOnMouseClicked(event -> handleConnectButtonClicked());

		interruptButton = new Button("X");
		interruptButton.setDisable(true);

		interruptButton.setOnMouseClicked(event -> handleDisconnectButtonClicked());

		hBox.getChildren().addAll(ipLabel, ipTextField, portLabel, portTextField, connectButton, interruptButton);
		vBox.getChildren().add(hBox);

		connectionErrorText = new ErrorText("");
		vBox.getChildren().add(connectionErrorText);

		openGamesListView = new ListView<>();

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.prefWidthProperty().bind(openGamesListView.widthProperty());
		scrollPane.prefHeightProperty().bind(openGamesListView.heightProperty());

		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

		scrollPane.setContent(openGamesListView);

		openGamesListView.setPrefSize(500, 250);
		scrollPane.setPadding(new Insets(10, 10, 10, 10));

		vBox.getChildren().add(scrollPane);

		stackPane.getChildren().add(vBox);

		return stackPane;
	}

	private GridPane createBottomPane() {
		GridPane gridPane = new GridPane();
		//gridPane.setStyle("-fx-background-color: #96acbb;");
		gridPane.setPadding(new Insets(10));

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);

		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50);

		gridPane.getColumnConstraints().addAll(col1, col2);

		HBox leftHbox = new HBox(10);
		leftHbox.setAlignment(Pos.CENTER_LEFT);
		networkInfoMessage = new Text();
		leftHbox.getChildren().add(networkInfoMessage);

		gridPane.add(leftHbox, 0, 0);
		HBox rightHbox = new HBox(10);
		rightHbox.setAlignment(Pos.CENTER_RIGHT);
		playerName = new Text();
		rightHbox.getChildren().add(playerName);
		playerName.setText(mainController.player.getPlayerName());
		connectionStatusText = new Text();
		connectionStatusText.setFont(Font.font(null, FontWeight.BOLD, 16));
		rightHbox.getChildren().add(connectionStatusText);

		gridPane.add(rightHbox, 1, 0);

		return gridPane;
	}

	private void handleConnectButtonClicked() {
		String ip = ipTextField.getText();
		int port = portTextField.getInt();

		if (nameTextField.isEmpty()) {
			connectionErrorText.setText("Name must not be empty.");
			return;
		}

		Player player0 = mainController.player;
		player0.setPlayerName(nameTextField.getText());
		playerName.setText(player0.getPlayerName());

		connectionErrorText.clear();
		toggleConnectionInputs(true);

		mainController.getGameClient().connect(ip, port);
		updateConnectionStatus(mainController.getGameClient().getCurrentNetworkStatus());
	}

	private void handlePlayButtonClicked() {
		mainController.send(ClientCommand.START_GAME.name());
		playButton.setDisable(true);
		mainController.setActiveScene(SceneName.WAITING);
	}

	private void handleDisconnectButtonClicked() {
		openGamesListView.getItems().clear();
		mainController.onApplicationClose();
	}

	/**
	 * Depending on parameter @value toggles
	 * ui controls.
	 * @param value enable buttons if true else disable
	 */
	private void toggleConnectionInputs(boolean value) {
		connectButton.setDisable(value);
		ipTextField.setDisable(value);
		portTextField.setDisable(value);
		nameTextField.setDisable(value);
		// Set button for interruption to opposite value
		interruptButton.setDisable(!value);

	}

	public void updateOpenGamesList(List<String> playersNamesList) {
		ObservableList<String> openGamesList = FXCollections.observableArrayList(playersNamesList);
		openGamesListView.setItems(openGamesList);
	}

	public void updateConnectionStatus(NetworkStatus networkStatus) {
		playButton.setDisable(true);
		switch (networkStatus) {
			case DISCONNECTED:
				connectionStatusText.setFill(Color.RED);
				networkInfoMessage.setText("");
				toggleConnectionInputs(false);

				mainController.setActiveScene(SceneName.MENU);
				break;
			case ATTEMPTING:
			case AUTO_ATTEMPTING:
				connectionStatusText.setFill(Color.YELLOW);
				break;
			case CONNECTED:
			case AUTO_CONNECTED:
				mainController.send(ClientCommand.JOIN_GAME.name(), mainController.player.getPlayerName());
				connectionStatusText.setFill(Color.GREEN);
				break;
		}
		connectionStatusText.setText(networkStatus.getText());
	}
}
