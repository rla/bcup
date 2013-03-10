package ee.pri.bcup.client.pool.field.chat;

import java.util.ArrayList;
import java.util.List;

import ee.pri.bcup.client.common.chat.BaseChatPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.common.Testing;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserverExitMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserverMessage;
import ee.pri.bcup.common.message.server.game.ServerGamePrivateMessage;
import ee.pri.bcup.common.message.server.game.ServerGameTextMessage;
import ee.pri.bcup.common.model.Player;

public class PoolChatTextPanel extends BaseChatPanel {
	private static final long serialVersionUID = 1L;
	
	private List<Player> observers = new ArrayList<Player>();

	public PoolChatTextPanel(PoolAppletContext appletContext) {
		super(appletContext);
		
		appletContext.processListeners(this, ListenerScope.GAME);
		
		setBorder(Testing.createComponentBorder());
	}
	
	@MessageReceiver(ServerGameObserverMessage.class)
	public void receive(ServerGameObserverMessage message) {
		Player player = getAppletContext().getPlayer(message.getPlayerId()); 
		observers.add(player);
		
		String text = getAppletContext().getMessage(
			"chat.notice.join",
			player.getName()
		);
			
		appendText(text);
	}
	
	@MessageReceiver(ServerGameTextMessage.class)
	public void receive(ServerGameTextMessage message) {
		String text = getAppletContext().getMessage(
			"chat.message",
			getAppletContext().getPlayer(message.getFromId()).getName(),
			message.getMessage()
		);
			
		appendText(text);
	}
	
	@MessageReceiver(ServerGamePrivateMessage.class)
	public void receive(ServerGamePrivateMessage message) {
		Player from = getAppletContext().getPlayer(message.getFromId());
		Player to = getAppletContext().getPlayer(message.getToId());
		
		appendText(getMessage("chat.message.private", from, to, message.getText()));
	}
	
	@MessageReceiver(ServerGameObserverExitMessage.class)
	public void receive(ServerGameObserverExitMessage message) {
		PoolAppletContext appletContext = (PoolAppletContext) getAppletContext();
		Player player = appletContext.getPlayer(message.getPlayerId());
		if (observers.contains(player)) {
			observers.remove(player);
			appendText(getMessage("chat.notice.game.returned", player));
		}
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		PoolAppletContext appletContext = (PoolAppletContext) getAppletContext();
		Player player = appletContext.getPlayer(message.getPlayerId());
		if (observers.contains(player)) {
			observers.remove(player);
			appendText(getMessage("chat.notice.game.returned", player));
		}
	}
}
