package ee.pri.bcup.server.context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import ee.pri.bcup.common.message.Message;
import ee.pri.bcup.common.message.server.pool.ServerPoolBallsMessage;
import ee.pri.bcup.common.message.server.pool.ServerPoolEndMessage;
import ee.pri.bcup.common.message.server.pool.ServerPoolSpeedMessage;
import ee.pri.bcup.common.model.pool.table.PoolGameSpec;
import ee.pri.bcup.server.model.ServerGame;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for pool game logics.
 *
 * @author Raivo Laanemets
 */
public class PoolManager implements InitializingBean {
	private static final Logger log = Logger.getLogger(PoolManager.class);

	/**
	 * Handles hit with speed specification.
	 */
	public void speed(
			ServerPlayer player,
			Double locationX,
			Double locationY,
			Double speedX,
			Double speedY,
			Double horisontalSpin,
			Double verticalSpin) {
		
		ServerGame game = player.getGame();
		Message.sendToAllExcept(game.getAll(), player, new ServerPoolSpeedMessage(
			locationX,
			locationY,
			speedX,
			speedY,
			horisontalSpin,
			verticalSpin
		));
	}
	
	/**
	 * Handles case when observer joins game when someone has already won.
	 */
	public void winObserver(ServerPlayer winner, ServerPlayer observer) {
		ServerPoolEndMessage sendMessage = new ServerPoolEndMessage();
		sendMessage.setWinnerPlayerId(winner.getId());
		observer.send(sendMessage);
	}
	
	/**
	 * Handles case when the observer should receive game synchronization.
	 */
	public void ballsObserver(ServerPlayer sender, ServerPlayer observer, PoolGameSpec spec) {
		spec.setHitTimeLeft(spec.getHitTimeLeft() - sender.getClientLatency() - observer.getClientLatency());
		observer.send(new ServerPoolBallsMessage(spec));
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("pool manager created");
	}

}
