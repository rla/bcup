package ee.pri.bcup.client.pool.chat;

import ee.pri.bcup.client.common.chat.BaseChatInput;
import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.common.message.client.ClientRoomMessage;
import ee.pri.bcup.common.message.client.ClientRoomPrivateMessage;
import ee.pri.bcup.common.model.Player;

public class ChatInput extends BaseChatInput {
	private static final long serialVersionUID = 1L;

	public ChatInput(AppletContext appletContext) {
		super(appletContext);
	}

	@Override
	protected void send(String text) {
		getAppletContext().send(new ClientRoomMessage(text));
	}

	@Override
	protected void sendToSelected(Player player, String text) {
		ClientRoomPrivateMessage message = new ClientRoomPrivateMessage();
		
		message.setPlayerId(player.getId());
		message.setText(text);
		
		getAppletContext().send(message);
	}

}
