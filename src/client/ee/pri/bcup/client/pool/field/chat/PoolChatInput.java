package ee.pri.bcup.client.pool.field.chat;

import ee.pri.bcup.client.common.chat.BaseChatInput;
import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.common.message.client.game.ClientGamePrivateTextMessage;
import ee.pri.bcup.common.message.client.game.ClientGameTextMessage;
import ee.pri.bcup.common.model.Player;

/**
 * Input component for pool game field chat.
 *
 * @author Raivo Laanemets
 */
public class PoolChatInput extends BaseChatInput {
	private static final long serialVersionUID = 1L;

	public PoolChatInput(AppletContext appletContext) {
		super(appletContext);
	}

	@Override
	protected void send(String text) {
		getAppletContext().send(new ClientGameTextMessage(text));
	}

	@Override
	protected void sendToSelected(Player player, String text) {
		ClientGamePrivateTextMessage message = new ClientGamePrivateTextMessage();
		message.setPlayerId(player.getId());
		message.setText(text);
		
		getAppletContext().send(message);
	}

}
