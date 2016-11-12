package main.networking;

import main.Constants;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;

public class ClientSocket extends GenericSocket {

	private String host;
	private ISocketListener socketListener;

	public ClientSocket(ISocketListener fxListener) {
		this(fxListener, Constants.instance().DEFAULT_HOST,
				Constants.instance().DEFAULT_PORT,
				Constants.instance().DEBUG_NONE);
	}

	public ClientSocket(ISocketListener fxListener,
	                      String host, int port) {
		this(fxListener, host, port, Constants.instance().DEBUG_NONE);
	}

	public ClientSocket(ISocketListener fxListener,
	                      String host, int port, int debugFlags) {
		super(port, debugFlags);
		this.host = host;
		this.socketListener = fxListener;
	}

	/**
	 * Called whenever a message is read from the socket.  In
	 * JavaFX, this method must be run on the main thread and
	 * is accomplished by the Platform.runLater() call.  Failure to do so
	 * *will* result in strange errors and exceptions.
	 * @param line Line of text read from the socket.
	 */
	@Override
	public void onMessage(final String line) {
		javafx.application.Platform.runLater(() -> socketListener.onMessage(line));
	}

	/**
	 * Called whenever the open/closed status of the Socket
	 * changes.  In JavaFX, this method must be run on the main thread and
	 * is accomplished by the Platform.runLater() call.  Failure to do so
	 * will* result in strange errors and exceptions.
	 * @param isClosed true if the socket is closed
	 */
	@Override
	public void onClosedStatus(final boolean isClosed) {
		javafx.application.Platform.runLater(() -> socketListener.onClosedStatus(isClosed));
	}

	/**
	 * Initialize the SocketClient up to and including issuing the accept()
	 * method on its socketConnection.
	 * @throws java.net.SocketException
	 */
	@Override
	protected void initSocketConnection() throws IOException {
		try {
			socketConnection = new Socket();
            /*
             * Allows the socket to be bound even though a previous
             * connection is in a timeout state.
             */
			socketConnection.setReuseAddress(true);

			int recvTimeout = 10000;
			socketConnection.setSoTimeout(recvTimeout);
            /*
             * Create a socket connection to the server
             */
			socketConnection.connect(new InetSocketAddress(host, port));

		} catch (BindException e) {
			throw new BindException("Error binding a socket to a local port. Local port can be already in use.");
		} catch (ConnectException e) {
			throw new ConnectException("Error connecting to a remote host and port.");
		} catch (NoRouteToHostException e) {
			throw new NoRouteToHostException("Error finding route to the remote host. Are you connected to the internet?");
		} catch (InterruptedIOException e) {
			throw new InterruptedIOException("Network timeout.");
		} catch (UnknownHostException e) {
			throw new UnknownHostException("Unknown host: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new SocketException(e.getMessage());
		} catch (IOException e) {
			throw new IOException("I/O Exception: " + e.getMessage());
		}
	}

	@Override
	public void onError(String errorMessage) {
		javafx.application.Platform.runLater(() -> socketListener.onError(errorMessage));
	}

	/**
	 * For SocketClient class, no additional work is required.  Method
	 * is null.
	 */
	@Override
	protected void closeAdditionalSockets() {}
}
