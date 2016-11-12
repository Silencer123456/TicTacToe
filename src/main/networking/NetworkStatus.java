package main.networking;

public enum NetworkStatus {
	DISCONNECTED("Disconnected"), ATTEMPTING("Attempting connection..."), CONNECTED("Connected"), AUTO_CONNECTED("Auto connected"), AUTO_ATTEMPTING("Auto attempting connection...");

	private String text;

	NetworkStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
