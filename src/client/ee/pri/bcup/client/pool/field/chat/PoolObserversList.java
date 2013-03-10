package ee.pri.bcup.client.pool.field.chat;

import java.awt.event.MouseEvent;

import ee.pri.bcup.client.common.chat.BaseChatInput;
import ee.pri.bcup.client.common.panel.BCupListPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.common.Testing;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserverExitMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserverMessage;

/**
 * List of game observers. For private messaging to work
 * also the main players must appear in the list.
 * 
 * See also {@link BaseChatInput}.
 *
 * @author Raivo Laanemets
 */
public class PoolObserversList extends BCupListPanel {
	private static final long serialVersionUID = 1L;
	
	public PoolObserversList(final PoolAppletContext appletContext) {
		super(appletContext);
		
		appletContext.processListeners(this, ListenerScope.GAME);
		setBorder(Testing.createComponentBorder());
		
		addElement(appletContext.getGameContext().getLeft());
		addElement(appletContext.getGameContext().getRight());
	}
	
	@MessageReceiver(ServerGameObserverMessage.class)
	public void receive(ServerGameObserverMessage message) {
		addElement(getAppletContext().getPlayer(message.getPlayerId()));
	}
	
	@MessageReceiver(ServerGameObserverExitMessage.class)
	public void receive(ServerGameObserverExitMessage message) {
		removeElement(getAppletContext().getPlayer(message.getPlayerId()));
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		removeElement(getAppletContext().getPlayer(message.getPlayerId()));
	}

	@Override
	protected void mousePressedItem(MouseEvent e) {}
	
}
