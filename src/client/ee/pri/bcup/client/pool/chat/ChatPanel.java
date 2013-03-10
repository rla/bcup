package ee.pri.bcup.client.pool.chat;

import ee.pri.bcup.client.common.Backgrounds;
import ee.pri.bcup.client.common.panel.BCupBackgroundedPanel;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.chat.games.ChatGamesPanel;
import ee.pri.bcup.client.pool.chat.users.ChatUserList;

public class ChatPanel extends BCupBackgroundedPanel {
	private static final long serialVersionUID = 1L;

	public ChatPanel(PoolAppletContext appletContext) {
		super(appletContext, Backgrounds.BACKGROUND_CHAT);
		
		ChatUserList userList = new ChatUserList(appletContext);
		add(userList.bounds(10, 130, 120, 440));
		
		add(new ChatProposalPanel(appletContext).bounds(215, 25, 800 - 225, 105 - 25));
		add(new ChatTextPanel(appletContext).bounds(140, 129, 800 - 300, 521 - 129));
		
		ChatInput input = new ChatInput(appletContext);
		input.setPlayers(userList);
		add(input.bounds(140, 547, 800 - 150, 568 - 547));
		
		
		add(new ChatGamesPanel(appletContext).bounds(650, 129, 800 - 650 - 10, 521 - 129));
	}
}
