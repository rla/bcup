package ee.pri.bcup.server.player;

import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.pool.ClientPoolBallsMessage;
import ee.pri.bcup.common.message.client.pool.ClientPoolEndMessage;
import ee.pri.bcup.common.message.client.pool.ClientPoolSpeedMessage;
import ee.pri.bcup.server.context.ServerContext;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for handling pool events.
 *
 * @author Raivo Laanemets
 */
public class PoolHelper extends AbstractBaseHelper {
	
	public PoolHelper(ServerPlayer player, ServerContext context) {
		super(player, context);
		
		player.getListenerSet().processListeners(this, ListenerScope.SERVER);
	}
	
	/**
	 * Handles pool game hit speed message.
	 */
	@MessageReceiver(ClientPoolSpeedMessage.class)
	public void receive(ClientPoolSpeedMessage message) {
		context.getPoolManager().speed(
			player,
			message.getLocationX(),
			message.getLocationY(),
			message.getSpeedX(),
			message.getSpeedY(),
			message.getHorisontalSpin(),
			message.getVerticalSpin()
		);
	}
	
	@MessageReceiver(ClientPoolEndMessage.class)
	public void receive(ClientPoolEndMessage message) {
		context.getPoolManager().winObserver(getPlayer(message.getWinnerPlayerId()), getPlayer(message.getPlayerId()));
	}
	
	@MessageReceiver(ClientPoolBallsMessage.class)
	public void receive(ClientPoolBallsMessage message) {
		context.getPoolManager().ballsObserver(player, getPlayer(message.getPlayerId()), message.getSpec());
	}
}
