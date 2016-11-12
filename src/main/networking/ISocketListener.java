package main.networking;

public interface ISocketListener {
	void onMessage(String line);
	void onClosedStatus(boolean isClosed);
	void onError(String errorMessage);
}
