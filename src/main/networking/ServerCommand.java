package main.networking;

public enum ServerCommand {
	GAMES_LIST("Games list"),
	GAME_INIT("MultiplayerGame started"),
	GAME_RECONNECT("MultiplayerGame reconnect"),
	NAME_ACQ("Name acknowledged"),
	PING("Ping"),
	PLAYER_DISCONNECT("Player disconnected"),
	INVALID("Invalid command"),
	GAME_JOINED("MultiplayerGame joined"),
	MESSAGE_INFO("Info message");

	String value;

	ServerCommand(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ServerCommand fromString(String text) {
		ServerCommand result = INVALID;
		if (text != null) {
			for (ServerCommand command : ServerCommand.values()) {
				if (text.equalsIgnoreCase(command.name())) {
					result = command;
					break;
				}
			}
		}
		return result;
	}
}
