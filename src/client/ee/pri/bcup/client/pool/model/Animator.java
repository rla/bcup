package ee.pri.bcup.client.pool.model;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import ee.pri.bcup.common.model.pool.table.Ball;
import ee.pri.bcup.common.model.pool.table.GameState;
import ee.pri.bcup.common.model.pool.table.Hole;
import ee.pri.bcup.common.model.pool.table.Table;
import ee.pri.bcup.common.model.pool.table.Wall;

public class Animator implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(Animator.class);
	
	private PoolGame context;
	private boolean moving = false;
	private boolean checkForMoving = false;
	private Thread thread;
	private boolean running = true;

	public Animator(final PoolGame context) {
		this.context = context;
		this.thread = new Thread(new Runnable() {
			@Override
			public void run() {
				long sleepTime = 30;
				long nextSleepTime = sleepTime;
				while (context.getGameState() != GameState.END && running) {
					long start = System.currentTimeMillis();
					try {
						Thread.sleep(nextSleepTime);
						for (int i = 0; i < 10; i++) {
							move();
						}
						context.getPoolFeedback().paintTable();
					} catch (InterruptedException e) {
						break;
					}
					long elapsed = System.currentTimeMillis() - start;
					long lag = Math.abs(elapsed - sleepTime);
					if (lag > 4) {
						nextSleepTime = sleepTime - lag % sleepTime;
					} else {
						nextSleepTime = sleepTime;
					}
				}
				
				log.debug("stopped");
			}
		});
		thread.start();
	}
	
	private void move() {
		if (!checkForMoving) {
			return;
		}
		
		Table table = context.getTable();
		
		moving = false;
		for (Ball ball : table.getBalls()) {
			if (ball.isOnTable()) {
				
				ball.location.x += ball.speed.x;
				ball.location.y += ball.speed.y; 

				ball.friction();
				moving |= ball.isMoving();
				ball.spin();
			}
		}
		for (Ball ball : table.getBalls()) {
			for (Hole hole : table.getHoles()) {
				hole.ballCheck(ball);
			}
		}
		List<Ball> balls = table.getBalls();
		for (int i = 0; i < balls.size(); i++) {
			for (int j = i + 1; j < balls.size(); j++) {
				balls.get(i).ballIntersection(balls.get(j));
			}
		}
		for (Ball ball : table.getBalls()) {
			for (Wall wall : table.getWalls()) {
				wall.ballIntersection(ball);
			}
		}
		
		if (!moving && checkForMoving) {
			context.ballsStopped();
			checkForMoving = false;
		}
	}

	public void hit() {
		checkForMoving = true;
	}

	public void stop() {
		running = false;
	}
	
}
