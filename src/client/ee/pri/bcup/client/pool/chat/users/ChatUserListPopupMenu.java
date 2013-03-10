package ee.pri.bcup.client.pool.chat.users;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ee.pri.bcup.client.common.menu.BCupPopupMenu;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.common.model.GameType;
import ee.pri.bcup.common.model.Player;

// FIXME comment this
// FIXME use enum instead of twoBall boolean
public class ChatUserListPopupMenu extends BCupPopupMenu implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private static Map<String, Long> timeouts = new HashMap<String, Long>();
	
	static {
		// FIXME could be an enum
		timeouts.put("a", 20L);
		timeouts.put("b", 40L);
		timeouts.put("c", 60L);
	}
	
	private JMenu playItem;
	private JList list;

	public ChatUserListPopupMenu(
			final PoolAppletContext appletContext,
			final JList list) {
		
		super(appletContext);
		
		this.list = list;
		
		playItem = new JMenu(getMessage("chat.play"));
		add(playItem);
		
		JMenuItem twoBall = new JMenuItem("2-ball (test)");
		twoBall.setActionCommand("a");
		twoBall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryProposeTo(e, GameType.POOL_2BALL);	
			}
		});
		add(twoBall);
		
		JMenuItem playItemA = new JMenuItem(getMessage("chat.play.a"));
		playItemA.setActionCommand("a");
		playItemA.addActionListener(this);
		playItem.add(playItemA);
		
		JMenuItem playItemB = new JMenuItem(getMessage("chat.play.b"));
		playItemB.setActionCommand("b");
		playItemB.addActionListener(this);
		playItem.add(playItemB);
		
		JMenuItem playItemC = new JMenuItem(getMessage("chat.play.c"));
		playItemC.setActionCommand("c");
		playItemC.addActionListener(this);
		playItem.add(playItemC);
	}

	public JMenuItem getPlayItem() {
		return playItem;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tryProposeTo(e, GameType.POOL_8BALL);
	}
	
	private void tryProposeTo(ActionEvent e, GameType gameType) {
		Player selected = (Player) list.getSelectedValue();
		if (selected != null
			&& !getAppletContext().getPlayer().getId().equals(selected.getId())) {
			
			getAppletContext().getProposeContext().proposeTo(selected, timeouts.get(e.getActionCommand()), gameType);
			selected.setGame(true);	
		}
	}
	
}