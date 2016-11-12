package main.networking;

public enum ClientCommand {
	JOIN_GAME("Join game"),
	START_GAME("Start game"),
	PING_ACK("Ping acknowledged"),
	MOVE("Move"),
	HEARTBEAT("Heartbeat"),
	DISCONNECT("Disconnect"),
	GAME_QUIT("Game quit");

	String value;

	ClientCommand(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
