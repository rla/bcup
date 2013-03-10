package ee.pri.bcup.client.pool.chat.users;

import java.awt.event.MouseEvent;

import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.common.panel.BCupListPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.server.ServerJoinRoomMessage;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.model.Player;

public class ChatUserList extends BCupListPanel {
	
	private static final long serialVersionUID = 1L;
	
	private ChatUserListPopupMenu menu;
	
	public ChatUserList(final PoolAppletContext appletContext) {
		super(appletContext);
		
		appletContext.processListeners(this, ListenerScope.APPLET);
		
		menu = new ChatUserListPopupMenu(appletContext, getList());
	}
	
	@MessageReceiver(ServerJoinRoomMessage.class)
	public void receive(ServerJoinRoomMessage message) {
		Player player = new Player();
		player.setId(message.getPlayerId());
		player.setName(message.getName());
		addElement(player);
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		removeElement(getAppletContext().getPlayer(message.getPlayerId()));
	}

	@Override
	protected void mousePressedItem(MouseEvent e) {
		AppletContext appletContext = getAppletContext();
		
    	Player selected = (Player) getSelectedValue();
    	menu.getPlayItem().setEnabled(
    		!appletContext.hasGame(selected.getId())
    		&& !selected.getId().equals(appletContext.getPlayer().getId())
    		&& !appletContext.getProposeContext().isProposeActive()
    	);
    	
    	menu.show(e.getComponent(), e.getX(), e.getY());
	}

}
