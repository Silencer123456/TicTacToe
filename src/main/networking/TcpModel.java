package main.networking;


import main.Constants;

import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

public class TcpModel {

	private final static Logger LOGGER
			= Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

	private boolean connected;
	private volatile boolean isAutoConnected;
	private GenericSocket socket;
	private NetworkStatus currentNetworkState;

	private ISocketMessageReceiver socketMessageReceiver;

	public TcpModel(ISocketMessageReceiver socketMessageReceiver) {
		this.socketMessageReceiver = socketMessageReceiver;
		initialize();
	}

	private void initialize() {
		setIsConnected(false);
		isAutoConnected = false;
		currentNetworkState = NetworkStatus.DISCONNECTED;

	}

	public void connect(String ip, int port) {
		currentNetworkState = NetworkStatus.ATTEMPTING;
		socket = new ClientSocket(
				new SocketListenerImpl(),
				ip,
				port,
				Constants.instance().DEBUG_ALL
		);
		socket.connect();
	}

	private class ShutDownThread extends Thread {
		@Override
		public void run() {
			if (socket != null) {
				if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
					LOGGER.info("ShutdownHook: Shutting down Server Socket");
				}
				socket.shutdown();
			}
		}
	}

	public void shutdown() {
		if (socket != null) {
			if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
				LOGGER.info("Shutting down Server Socket");
			}
			socket.shutdown();
		}
	}

	public void sendMessage(String... tokens) {
	    if (socket == null) return;

		String delimiter = ";";

		StringBuilder finalMessage = new StringBuilder();

		for (String command : tokens) {
			finalMessage.append(command);
			finalMessage.append(delimiter);
		}

		String message = finalMessage.toString().trim().substring(0, finalMessage.toString().length()-1);
		message += ">";

		byte []data = message.getBytes();
		socket.sendMessage(data);
	}

	private void autoConnect() {
		new Thread() {
			@Override
			public void run() {
				while (isAutoConnected) {
					if (!isConnected()) {
						socket = new ClientSocket(
								new SocketListenerImpl(),
								Constants.instance().DEFAULT_HOST,
								Constants.instance().DEFAULT_PORT,
								Constants.instance().DEBUG_ALL
						);
						socket.connect();
					}
					waitForDisconnect();
					try {
						Thread.sleep(Constants.instance().DEFAULT_RETRY_INTERVAL);
					} catch (InterruptedException ex) {
					}
				}
			}
		}.start();
	}

	private class SocketListenerImpl implements ISocketListener {

		@Override
		public void onMessage(String line) {
			if (line != null && !line.equals("")) {
				socketMessageReceiver.handleReceivedMessage(line);
			}
		}

		@Override
		public void onClosedStatus(boolean isClosed) {
			if (isClosed) {
				notifyDisconnected();
				if (isAutoConnected) {
					currentNetworkState = NetworkStatus.AUTO_ATTEMPTING;
				} else {
					currentNetworkState = NetworkStatus.DISCONNECTED;
				}
			} else {
				setIsConnected(true);
				if (isAutoConnected) {
					currentNetworkState = NetworkStatus.AUTO_CONNECTED;
				} else {
					currentNetworkState = NetworkStatus.CONNECTED;
				}
			}
			socketMessageReceiver.onNetworkStateChanged(currentNetworkState);
		}

		@Override
		public void onError(String errorMessage) {
			socketMessageReceiver.onError(errorMessage);
		}
	}

	/*
	 * Synchronized method set up to wait until there is no socket connection.
     * When notifyDisconnected() is called, waiting will cease.
     */
	private synchronized void waitForDisconnect() {
		while (connected) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	/*
	 * Synchronized method responsible for notifying waitForDisconnect()
	 * method that it's OK to stop waiting.
	 */
	private synchronized void notifyDisconnected() {
		connected = false;
		notifyAll();
	}

	/*
	 * Synchronized method to set isConnected boolean
	 */
	private synchronized void setIsConnected(boolean connected) {
		this.connected = connected;
	}

	/*
	 * Synchronized method to check for value of connected boolean
	 */
	private synchronized boolean isConnected() {
		return (connected);
	}

	public NetworkStatus getCurrentNetworkStatus() {
		return currentNetworkState;
	}
}
