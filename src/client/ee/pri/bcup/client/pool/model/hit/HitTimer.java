package ee.pri.bcup.client.pool.model.hit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.apache.log4j.Logger;

import ee.pri.bcup.client.pool.model.PoolGame;

/**
 * Helper class for detecting hit timeout.
 * 
 * @author Raivo Laanemets
 */
public class HitTimer {
	private static final Logger log = Logger.getLogger(HitTimer.class);
	
	private long lastHitTime = 0;
	private long maxTimeInSeconds = 0;
	private PoolGame game;
	private Timer timer;
	private long lastRemainingTime = 0;
	
	public HitTimer(PoolGame game, long maxTimeInSeconds) {
		this.game = game;
		this.maxTimeInSeconds = maxTimeInSeconds;
	}
	
	public void start() {
		start((int) (maxTimeInSeconds * 1000));
	}

	public void start(int timeout) {
		stop();
		timer = new Timer(0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.hitTimeoutOccurred();
			}
		});
		timer.setInitialDelay(timeout);
		timer.setRepeats(false);
		timer.start();
		lastHitTime = System.currentTimeMillis() - ((int) (maxTimeInSeconds * 1000) - timeout);
	}
	
	public void stop() {
		if (timer != null) {
			timer.stop();
			log.debug("stopped timer: " + timer);
		}
	}
	
	public long getRemainingTime() {
		if (timer.isRunning()) {
			lastRemainingTime = maxTimeInSeconds * 1000 - (System.currentTimeMillis() - lastHitTime);
		}
		return lastRemainingTime;
	}

	public void setRemainingTime(long hitTimeLeft) {
		lastHitTime = System.currentTimeMillis() - hitTimeLeft;
	}
	
}
