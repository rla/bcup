package ee.pri.bcup.client.pool.chat;

import ee.pri.bcup.client.common.chat.BaseChatPanel;
import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.server.ServerJoinRoomMessage;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.ServerRoomMessage;
import ee.pri.bcup.common.message.server.ServerRoomPrivateMessage;
import ee.pri.bcup.common.message.server.game.ServerGameExitMessage;
import ee.pri.bcup.common.message.server.game.ServerGameStartedMessage;
import ee.pri.bcup.common.model.Player;

/**
 * Component for displaying chat events in the pool main room.
 * 
 * @author Raivo Laanemets
 */
public class ChatTextPanel extends BaseChatPanel {
	private static final long serialVersionUID = 1L;

	public ChatTextPanel(AppletContext appletContext) {
		super(appletContext);
		
		appletContext.processListeners(this, ListenerScope.APPLET);
	}
	
	@MessageReceiver(ServerGameStartedMessage.class)
	public void receive(ServerGameStartedMessage message) {
		// FIXME NPE here?
		String text = getMessage(
			"chat.game.started",
			getAppletContext().getPlayer(message.getPlayerAId()).getName(),
			getAppletContext().getPlayer(message.getPlayerBId()).getName()
		);
			
		appendText(text);
	}
	
	@MessageReceiver(ServerRoomMessage.class)
	public void receive(ServerRoomMessage message) {
		String[] lines = message.getMessage().split("\\n");
		
		// FIXME generalize into base class?
		for (String line : lines) {
			String text = getMessage(
				"chat.message",
				getAppletContext().getPlayer(message.getSenderId()).getName(),
				line
			);
				
			appendText(text);
		}
	}
	
	@MessageReceiver(ServerJoinRoomMessage.class)
	public void receive(ServerJoinRoomMessage message) {
		appendText(getMessage(
			"chat.notice.join",
			message.getName()
		));
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		appendText(getMessage(
			"chat.notice.part",
			getAppletContext().getPlayer(message.getPlayerId()).getName()
		));
	}
	
	@MessageReceiver(ServerGameExitMessage.class)
	public void receive(ServerGameExitMessage message) {
		appendText(getMessage(
			"chat.notice.game.returned",
			getAppletContext().getPlayer(message.getPlayerId()).getName()
		));		
	}
	
	@MessageReceiver(ServerRoomPrivateMessage.class)
	public void receive(ServerRoomPrivateMessage message) {
		Player from = getAppletContext().getPlayer(message.getFromId());
		Player to = getAppletContext().getPlayer(message.getToId());
		
		appendText(getMessage("chat.message.private", from, to, message.getText()));
	}
}
