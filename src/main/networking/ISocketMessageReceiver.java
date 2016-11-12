package main.networking;

public interface ISocketMessageReceiver {
	/**
	 * Is called whenever a network message is received.
	 * @param text - received message string
	 */
	void handleReceivedMessage(String text);
	void onNetworkStateChanged(NetworkStatus networkStatus);
	void onError(final String errorText);
}
