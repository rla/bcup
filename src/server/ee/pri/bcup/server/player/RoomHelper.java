package ee.pri.bcup.server.player;

import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.ClientJoinRoomMessage;
import ee.pri.bcup.common.message.client.ClientRoomMessage;
import ee.pri.bcup.common.message.client.ClientRoomPrivateMessage;
import ee.pri.bcup.server.context.ServerContext;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for managing rooms.
 *
 * @author Raivo Laanemets
 */
public class RoomHelper extends AbstractBaseHelper {
	
	public RoomHelper(ServerPlayer player, ServerContext context) {
		super(player, context);
		
		player.getListenerSet().processListeners(this, ListenerScope.SERVER);
	}
	
	@MessageReceiver(ClientJoinRoomMessage.class)
	public void receive(ClientJoinRoomMessage message) {
		context.getRoomManager().join(player, getRoom(message.getRoomId()));
	}
	
	@MessageReceiver(ClientRoomMessage.class)
	public void receive(ClientRoomMessage message) {
		context.getRoomManager().message(player, message.getMessage());
	}
	
	@MessageReceiver(ClientRoomPrivateMessage.class)
	public void receive(ClientRoomPrivateMessage message) {
		context.getRoomManager().sendPrivateMessage(player, getPlayer(message.getPlayerId()), message.getText());
	}
}
