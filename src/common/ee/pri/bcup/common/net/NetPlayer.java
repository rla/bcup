package ee.pri.bcup.common.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

import ee.pri.bcup.common.message.Message;
import ee.pri.bcup.common.model.Player;
import ee.pri.bcup.common.util.IOUtils;
import ee.pri.bcup.common.util.ThreadUtils;

/**
 * Base class for representing players on both client
 * and server side. Handles network connection.
 */
public abstract class NetPlayer extends Player {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(NetPlayer.class);

	private static final int LOOP_SLEEP = 2000;
	
	private final Socket socket;
	private final BufferedReader reader;
	private final PrintStream writer;
	private final int clientTimeout;
	
	private Thread thread;
	
	private boolean running;
	
	public NetPlayer(Socket socket, int clientTimeout) throws IOException {
		this.socket = socket;
		this.socket.setSoTimeout(LOOP_SLEEP);
		this.socket.setTcpNoDelay(true);
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		this.writer = new PrintStream(socket.getOutputStream(), true, "UTF-8");
		this.running = true;
		this.clientTimeout = clientTimeout;
	}
	
	@Override
	public void setName(String name) {
		super.setName(name);
		if (thread != null) {
			thread.setName(name);
		}
	}

	public void start() {
		thread = new Thread(new PlayerLoop());
		thread.start();
	}
	
	/**
	 * Method for sending message to the other party.
	 */
	// FIXME this must be written to never block.
	public void send(Message message) {
		writer.println(message.makeMessage());
		writer.flush();
		
		if (writer.checkError()) {
			log.error(this + " has write error");
		}
	}
	
	private void receive(String line) {
		Message message = Message.parse(line);
		if (message != null) {
			try {
				receiveMessage(message);
			} catch (Exception e) {
				// FIXME should consider disconnect?
				e.printStackTrace();
			}
		}
	}
	
	protected abstract void receiveMessage(Message message);
	
	protected abstract void handleDisconnect();
	
	public void terminate() {
		running = false;
	}
	
	public void terminateAndStop() {
		terminate();
		ThreadUtils.join(thread);
	}

	private class PlayerLoop implements Runnable {
		private long lastReceiveTime;

		public void run() {
			lastReceiveTime = System.currentTimeMillis();
			while (running) {
				try {
					String line = reader.readLine();
					if (line == null) {
						handleDisconnect();
						break;
					}
					lastReceiveTime = System.currentTimeMillis();
					receive(line);
				} catch (SocketTimeoutException e) {
					long time = System.currentTimeMillis();
					if (time - lastReceiveTime > clientTimeout) {
						log.debug("disconnecing " + this + " because exception timeout occurred");
						handleDisconnect();
						break;
					}
				} catch (Exception e) {
					log.debug("disconnecing " + this + " because exception " + e.getMessage() + " occurred");
					handleDisconnect();
					break;
				}
			}
			
			IOUtils.quietClose(writer);
			IOUtils.quietClose(reader);
			IOUtils.quietClose(socket);
		}
		
	}
	
}
