package ee.pri.bcup.client.pool.chat.games;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JMenuItem;

import ee.pri.bcup.client.common.menu.BCupPopupMenu;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.PoolObserverContext;
import ee.pri.bcup.common.message.client.game.ClientGameObserveMessage;
import ee.pri.bcup.common.model.Game;

/**
 * Popup menu for games list.
 * 
 * @author Raivo Laanemets
 */
public class ChatGamesPopupMenu extends BCupPopupMenu {
	private static final long serialVersionUID = 1L;

	public ChatGamesPopupMenu(final PoolAppletContext appletContext, final JList list) {
		super(appletContext);
		
		JMenuItem observeItem = new JMenuItem(getMessage("chat.observe"));
		observeItem.setEnabled(true);
		observeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Game game = (Game) list.getSelectedValue();

				// FIXME move game inside observercontext
				PoolObserverContext observerContext = new PoolObserverContext(game.getFirst(), game.getSecond(), game.getHitTimeout(), game.getGameType());
				appletContext.setObserverContext(observerContext);
				appletContext.getPlayer().send(new ClientGameObserveMessage(game.getId()));
			}
		});
		
		add(observeItem);
	}
	
}
