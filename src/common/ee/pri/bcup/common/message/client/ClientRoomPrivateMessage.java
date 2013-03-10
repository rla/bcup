package ee.pri.bcup.common.message.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Message for sending private messages in the main chat room.
 *
 * @author Raivo Laanemets
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ClientRoomPrivateMessage extends ClientMessage {
	private Long playerId;
	private String text;
}
