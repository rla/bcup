package ee.pri.bcup.server.context;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import ee.pri.bcup.server.dao.PlayerDao;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Class for handling currently connected players at the server.
 * It is responsible for registering new players and removing
 * them on disconnect.
 */
public class PlayerManager implements
	DisconnectListener, InitializingBean, DisposableBean,
	ApplicationContextAware, Runnable {
	
	private static final Logger log = Logger.getLogger(PlayerManager.class);
	
	private static final int LOOP_SLEEP = 2000;
	
	private ServerContext context;
	private int port;
	private String shutdownMagic;
	private ConfigurableApplicationContext applicationContext;
	private boolean running = false;
	private ServerSocket serverSocket;
	private Thread thread;
	private PlayerDao playerDao;
	
	@Required
	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	@Required
	public void setPort(int port) {
		this.port = port;
	}
	
	@Required
	public void setShutdownMagic(String shutdownMagic) {
		this.shutdownMagic = shutdownMagic;
	}
	
	/**
	 * Entry point when the given player disconnects.
	 * Disconnect might happen because the connection
	 * timeouts or the client leaves the Applet.
	 */
	public void disconnect(ServerPlayer player) {
		if (!player.isDisconnected()) {
			context.disconnected(player);
			player.setDisconnected(true);
		}
	}
	
	/**
	 * The player send ping response.
	 */
	public void ping(ServerPlayer player) {
		player.setClientLatency((System.currentTimeMillis() - player.getLastPingTime()) / 2);
	}
	
	/**
	 * Special method for terminating the server.
	 * @param shutdownMagic magic word that allows shutdown.
	 */
	public void stop(String shutdownMagic) {
		log.info("stopping server");
		if (this.shutdownMagic.equals(shutdownMagic)) {
			log.debug("spawning shutdown thread");
			new Thread(new Runnable() {
				@Override
				public void run() {
					applicationContext.close();
					log.debug("shutdown thread finished");
				}
			}).start();
		}
	}

	private void register(Socket socket) {
		try {
			
			ServerPlayer player = new ServerPlayer(socket, context, 40000);
			playerDao.store(player);
			player.start();

			log.info("player " + player.getId() + " joined (" + socket.getInetAddress() + ")");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void disconnected(ServerPlayer player) {
		log.debug("deleting player " + player);
		playerDao.delete(player);
	}

	@Override
	public void destroy() throws Exception {
		log.debug("destroying player manager");
		
		for (ServerPlayer player : playerDao.all()) {
			player.terminateAndStop();
		}
		
		running = false;
		thread.join();
		serverSocket.close();
	}

	@Required
	public void setContext(ServerContext context) {
		this.context = context;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("created player manager");
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(LOOP_SLEEP);
		running = true;
		thread = new Thread(this, "playerManager");
		thread.start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	@Override
	public void run() {
		log.info("started");
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				register(socket);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				log.error("Problem with accepting connection", e);
			}
		}
		log.info("stopped");
	}

}
