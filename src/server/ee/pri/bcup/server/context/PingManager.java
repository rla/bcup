package ee.pri.bcup.server.context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import ee.pri.bcup.common.message.server.ServerPingMessage;
import ee.pri.bcup.server.dao.PlayerDao;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for periodically pinging players.
 * Pinging is used for detecting disconnected users.
 *
 * @author Raivo Laanemets
 */
public class PingManager implements Runnable, InitializingBean, DisposableBean {
	private static final Logger log = Logger.getLogger(PingManager.class);
	
	private static final int LOOP_SLEEP = 2000;
	
	private boolean running;
	private int pingInterval;
	private PlayerDao playerDao;

	@Required
	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	@Required
	public void setPingInterval(int pingInterval) {
		this.pingInterval = pingInterval;
	}

	public void run() {
		log.info("started");
		long lastPingTime = System.currentTimeMillis();
		while (running) {
			try {
				Thread.sleep(LOOP_SLEEP);
			} catch (InterruptedException e) {
				continue;
			}
			long time = System.currentTimeMillis();
			if (time - lastPingTime > pingInterval) {
				for (ServerPlayer player : playerDao.all()) {
					player.setLastPingTime(System.currentTimeMillis());
					player.send(new ServerPingMessage());
				}
				lastPingTime = time;
			}
		}
		log.info("stopped");
	}

	public void afterPropertiesSet() throws Exception {
		running = true;
		new Thread(this, "pingManager").start();
		log.debug("ping manager created and started");
	}

	public void destroy() throws Exception {
		running = false;
		log.debug("ping manager destroyed");
	}
	
}
