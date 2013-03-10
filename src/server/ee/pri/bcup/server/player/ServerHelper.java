package ee.pri.bcup.server.player;

import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.AuthenticationRequestMessage;
import ee.pri.bcup.common.message.client.ClientLeaveMessage;
import ee.pri.bcup.common.message.client.ClientPingMessage;
import ee.pri.bcup.common.message.client.ClientShutdownMessage;
import ee.pri.bcup.server.context.ServerContext;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for basic functionality.
 *
 * @author Raivo Laanemets
 */
public class ServerHelper extends AbstractBaseHelper {	
	
	public ServerHelper(ServerPlayer player, ServerContext context) {
		super(player, context);
		
		player.getListenerSet().processListeners(this, ListenerScope.SERVER);
	}
	
	/**
	 * Executes user authentication.
	 */
	@MessageReceiver(AuthenticationRequestMessage.class)
	public void receive(AuthenticationRequestMessage message) {
		context.getAuthenticationManager().authenticate(player, message.getUserId(), message.getDatabaseEnvKey(), message.getVersion());
	}
	
	/**
	 * Executes shutdown. This action return immediately
	 * by tearning down Spring IOC context in a separate thread.
	 */
	@MessageReceiver(ClientShutdownMessage.class)
	public void receive(ClientShutdownMessage message) {
		context.getPlayerManager().stop(message.getMagicWord());
	}
	
	/**
	 * Handles message when the client leaves the server. 
	 */
	@MessageReceiver(ClientLeaveMessage.class)
	public void receive(ClientLeaveMessage message) {
		context.getPlayerManager().disconnect(player);
	}
	
	/**
	 * Handles ping response from the client.
	 */
	@MessageReceiver(ClientPingMessage.class)
	public void receive(ClientPingMessage message) {
		context.getPlayerManager().ping(player);
	}
}
