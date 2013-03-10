package ee.pri.bcup.common.message.client.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Message to send private text message to another player in a game.
 *
 * @author Raivo Laanemets
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ClientGamePrivateTextMessage extends ClientGameMessage {
	private Long playerId;
	private String text;
}
