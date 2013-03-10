package ee.pri.bcup.server.player;

import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.game.ClientGameExitMessage;
import ee.pri.bcup.common.message.client.game.ClientGameLoseMessage;
import ee.pri.bcup.common.message.client.game.ClientGameObserveMessage;
import ee.pri.bcup.common.message.client.game.ClientGamePrivateTextMessage;
import ee.pri.bcup.common.message.client.game.ClientGameTextMessage;
import ee.pri.bcup.server.context.ServerContext;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for working with games-related general logics.
 *
 * @author Raivo Laanemets
 */
public class GameHelper extends AbstractBaseHelper {
	
	public GameHelper(ServerPlayer player, ServerContext context) {
		super(player, context);
		
		player.getListenerSet().processListeners(this, ListenerScope.SERVER);
	}
	
	/**
	 * Handles game lose.
	 */
	@MessageReceiver(ClientGameLoseMessage.class)
	public void receive(ClientGameLoseMessage message) {
		context.getRoomManager().lost(player);
	}
	
	/**
	 * Handles game observing.
	 */
	@MessageReceiver(ClientGameObserveMessage.class)
	public void receive(ClientGameObserveMessage message) {
		context.getRoomManager().observe(player, getGame(message.getGameId()));
	}
	
	/**
	 * Handles text message from one of the game parties (player or observer).
	 */
	@MessageReceiver(ClientGameTextMessage.class)
	public void receive(ClientGameTextMessage message) {
		context.getRoomManager().text(player, message.getMessage());
	}
	
	@MessageReceiver(ClientGamePrivateTextMessage.class)
	public void receive(ClientGamePrivateTextMessage message) {
		context.getRoomManager().sendGamePrivateMessage(player, getPlayer(message.getPlayerId()), message.getText());
	}
	
	@MessageReceiver(ClientGameExitMessage.class)
	public void receive(ClientGameExitMessage message) {
		context.getRoomManager().exitGame(player);
	}
	
}
