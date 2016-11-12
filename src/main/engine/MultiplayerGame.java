package main.engine;

import javafx.scene.control.Alert;
import main.Constants;
import main.MainController;
import main.game.MenuScene;
import main.game.SceneName;
import main.networking.*;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiplayerGame implements ISocketMessageReceiver {

    private TcpModel tcpModel;
    private MainController mainController;

    public MultiplayerGame(MainController mainController) {
        this.mainController = mainController;
        tcpModel = new TcpModel(this);
    }

    @Override
    public void handleReceivedMessage(String text) {
        String[] tokens = text.split(Constants.instance().COMMANDS_DELIMITER);
        if (tokens.length == 0) return;

        String command = tokens[0];
        String[] data = Arrays.copyOfRange(tokens, 1, tokens.length);

        MenuScene menuScene = (MenuScene) mainController.getScene(SceneName.MENU);

        ServerCommand commandEnum = ServerCommand.fromString(command);

        switch (commandEnum) {
            case GAMES_LIST:
                menuScene.updateOpenGamesList(new ArrayList<>(Arrays.asList(data)));
                break;
            case GAME_INIT:
                String gameId = data[0];
                String boardSize = data[1];
                mainController.newGame(gameId, boardSize);
                break;
            case GAME_RECONNECT:
                // temp!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                mainController.newGame(data[0], data[1]);
                break;
            case NAME_ACQ:
                if (data[0].equals("1")) {
                    menuScene.playButton.setDisable(false);
                    menuScene.nameTextField.setVisible(false);
                } else {
                    String errorMsg = data[1];
                    menuScene.showDialog(Alert.AlertType.ERROR, "Bad name", "Choose a different name", errorMsg);
                    mainController.player.setPlayerName("");
                }
                break;
            case PING:
                sendMessage(ClientCommand.PING_ACK.name());
                break;
            case PLAYER_DISCONNECT:
                mainController.getActiveScene().showDialog(
                        Alert.AlertType.INFORMATION, "INFO", data[0], "Opponent has left the game");
                break;
            case INVALID:
                break;
            case GAME_JOINED:
                menuScene.networkInfoMessage.setText(data[0]);
                break;
            case MESSAGE_INFO:
                menuScene.networkInfoMessage.setText(data[0]);
                break;
        }
    }

    @Override
    public void onNetworkStateChanged(NetworkStatus networkStatus) {
        MenuScene menuScene = (MenuScene) mainController.getScene(SceneName.MENU);
        menuScene.updateConnectionStatus(networkStatus);
    }

    @Override
    public void onError(String errorText) {

    }

    public void sendMessage(String... tokens) {
        tcpModel.sendMessage(tokens);
    }

    public TcpModel getTcpModel() {
        return tcpModel;
    }
}
