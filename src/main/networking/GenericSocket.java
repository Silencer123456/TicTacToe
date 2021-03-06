package main.networking;

import main.Constants;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.logging.Logger;

public abstract class GenericSocket implements ISocketListener {

	private final static Logger LOGGER =
			Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

	public int port;
	protected Socket socketConnection = null;
	private DataOutputStream output = null;
	private BufferedReader input = null;
	private boolean ready = false;
	private Thread socketReaderThread;
	private Thread setupThread;
	private int debugFlags;

	public GenericSocket(int port) {
		this(port, Constants.instance().DEBUG_NONE);
	}

	public GenericSocket(int port, int debugFlags) {
		this.port = port;
		this.debugFlags = debugFlags;
	}

	public GenericSocket() {
		this(Constants.instance().DEFAULT_PORT,
				Constants.instance().DEBUG_NONE);
	}

	/**
	 * Returns true if the specified debug flag is set.
	 *
	 * @param flag Debug flag in question
	 * @return true if the debug flag 'flag' is set.
	 */
	public boolean debugFlagIsSet(int flag) {
		return ((flag & debugFlags) != 0);
	}

	/**
	 * Turn on debugging option.
	 *
	 * @param flags The debugging flags to enable
	 */
	public void setDebugFlags(int flags) {
		debugFlags = flags;
	}

	/**
	 * Get the current set of debug flags.
	 *
	 * @return the current debug flag bitmask
	 */
	public int getDebugFlags() {
		return debugFlags;
	}

	/**
	 * Turn off debugging option.
	 */
	public void clearDebugFlags() {
		debugFlags = Constants.instance().
				DEBUG_NONE;
	}

	/**
	 * Set up a connection in the background.  This method returns no status,
	 * however the displayState(boolean) method will be called when the
	 * status of the socket changes, either opened or closed (for whatever
	 * reason).
	 */
	public void connect() {
		try {
	        /*
             * Background thread to set up and open the input and
             * output data streams.
             */
			setupThread = new SetupThread();
			setupThread.start();
            /*
             * Background thread to continuously read from the input stream.
             */
			socketReaderThread = new SocketReaderThread();
			socketReaderThread.start();
		} catch (Exception e) {
			if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
				LOGGER.info(e.getMessage());
			}
			System.out.println("doslo k sitove chybe");
		}
	}

	/**
	 * Shutdown and close GenericSocket instance in an orderly fashion.
	 * As per the Java Socket API, once a Socket has been closed, it is not
	 * available for further networking use (i.e. can't be reconnected
	 * or rebound) A new Socket needs to be created.
	 */
	public void shutdown() {
		close();
	}

	public void interruptConnection() {
		setupThread.interrupt();
	}

	/**
	 * Close down the GenericSocket infrastructure.  As per the Java Socket
	 * API, once a Socket has been closed, it is not available for
	 * further networking use (i.e. can't be reconnected or rebound).
	 * A new Socket needs to be created.
	 * <p>
	 * For certain implementations (e.g. ProviderSocket), the
	 * closeAdditionalSockets() method may need to be more than just a
	 * null method.
	 */
	private void close() {
		try {
			if (socketConnection != null && !socketConnection.isClosed()) {
				socketConnection.close();
			}
            /*
             * closeAdditionalSockets() has to be implemented in a subclass.
             * In some cases nothing may be requied (null method), but for
             * others (e.g. SocketServer), the method can be used to
             * close additional sockets.
             */
			closeAdditionalSockets();
			if (debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
				LOGGER.info("Connection closed");
			}
            /*
             * The displayState() method has to be implemented by
             * a sublclass.  If used in conjunction with JavaFX,
             * use Platform.runLater() to force this method to run
             * on the main thread.
             */
			onClosedStatus(true);
		} catch (IOException e) {
			if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
				LOGGER.info(e.getMessage());
			}
			onError(e.getMessage());
		}
	}

	/**
	 * This method is invoked to do instance-specific socket initialization.
	 * Due to the different ways that sockets are set up (e.g.
	 * ServerSocket vs Socket), the implementation details go here.
	 * Initialization up to and including either accept() or connect() take
	 * place here.
	 *
	 * @throws java.net.SocketException
	 */
	protected abstract void initSocketConnection() throws IOException;

	/**
	 * This method is called to close any additional sockets that are
	 * internally used.  In some cases (e.g. SocketClient), this method
	 * should do nothing.  In others (e.g. SocketServer), this method should
	 * close the internal ServerSocket instance.
	 */
	protected abstract void closeAdditionalSockets();

	/*
     * Synchronized method set up to wait until the SetupThread is
     * sufficiently initialized.  When notifyReady() is called, waiting
     * will cease.
     */
	private synchronized void waitForReady() {
		while (!ready) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}

	/*
	 * Synchronized method responsible for notifying waitForReady()
	 * method that it's OK to stop waiting.
	 */
	private synchronized void notifyReady() {
		ready = true;
		notifyAll();
	}

	/**
	 * Send a message in the form of a String to the socket.  A NEWLINE will
	 * automatically be appended to the message.
	 *
	 * @param data The String message to send
	 */
	public void sendMessage(byte[] data) {
		try {
			output.write(data, 0, data.length);
			//output.newLine();
			output.flush();
			if (debugFlagIsSet(Constants.instance().DEBUG_SEND)) {
				String logMsg = "send> " + new String(data);
				LOGGER.info(logMsg);
			}
		} catch (IOException e) {
			if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
				LOGGER.info(e.getMessage());
			}
		}
	}

	private class SetupThread extends Thread {

		@Override
		public void run() {
			try {
				initSocketConnection();
				if (socketConnection != null && !socketConnection.isClosed()) {
                    /*
                     * Get input and output streams
                     */
					input = new BufferedReader(new InputStreamReader(
							socketConnection.getInputStream()));
					output = new DataOutputStream(socketConnection.getOutputStream());
					output.flush();
				}
                /*
                 * Notify SocketReaderThread that it can now start.
                 */

			} catch (IOException e) {
				if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
					LOGGER.info(e.getMessage());
				}
				onError(e.getMessage());
				e.printStackTrace();
				/*
				 * This will notify the SocketReaderThread that it should exit.(moved)
				 */
			}
			notifyReady();
		}
	}

	private class SocketReaderThread extends Thread {

		@Override
		public void run() {
            /*
             * Wait until the socket is set up before beginning to read.
             */
			waitForReady();
            /*
             * Now that the readerThread has started, it's safe to inform
             * the world that the socket is open, if in fact, it is open.
             * If used in conjunction with JavaFX, use Platform.runLater()
             * when implementing this method to force it to run on the main
             * thread.
             */
			if (socketConnection != null && socketConnection.isConnected()) {
				onClosedStatus(false);
			}
            /*
             * Read from input stream one line at a time
             */
			try {
				if (input != null) {
					String line;
					while ((line = input.readLine()) != null) {
						if (debugFlagIsSet(Constants.instance().DEBUG_RECV)) {
							String logMsg = "recv> " + line;
							LOGGER.info(logMsg);
						}
                        /*
                         * The onMessage() method has to be implemented by
                         * a sublclass.  If used in conjunction with JavaFX,
                         * use Platform.runLater() to force this method to run
                         * on the main thread.
                         */
						onMessage(line);
					}
				}
			} catch (IOException e) {
				if (debugFlagIsSet(Constants.instance().DEBUG_EXCEPTIONS)) {
					LOGGER.info(e.getMessage());
				}
				onError(e.getMessage());
			} finally {
				close();
			}
		}
	}
}
