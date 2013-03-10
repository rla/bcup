package ee.pri.bcup.common.message.server;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Private message from one player to another.
 *
 * @author Raivo Laanemets
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServerRoomPrivateMessage extends ServerMessage {
	private Long fromId;
	private Long toId;
	private String text;
}
