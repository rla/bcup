package ee.pri.bcup.client.pool.chat.games;

import java.awt.event.MouseEvent;

import ee.pri.bcup.client.common.panel.BCupListPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.game.ServerGameExitMessage;
import ee.pri.bcup.common.message.server.game.ServerGameStartedMessage;
import ee.pri.bcup.common.model.Game;

public class ChatGamesPanel extends BCupListPanel {
	private static final long serialVersionUID = 1L;
	
	private ChatGamesPopupMenu menu;

	public ChatGamesPanel(PoolAppletContext appletContext) {
		super(appletContext);
		appletContext.processListeners(this, ListenerScope.APPLET);
		
		menu = new ChatGamesPopupMenu(appletContext, getList());
	}
	
	@MessageReceiver(ServerGameStartedMessage.class)
	public void receive(ServerGameStartedMessage message) {
		update();
	}
	
	@MessageReceiver(ServerGameExitMessage.class)
	public void receive(ServerGameExitMessage message) {
		update();
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		update();
	}
	
	private void update() {
		clear();
		for (Game game : getAppletContext().getGames()) {
			addElement(game);
		}
	}

	@Override
	protected void mousePressedItem(MouseEvent e) {
		menu.show(e.getComponent(), e.getX(), e.getY());
	}

}
