package ee.pri.bcup.common.message.server.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Private message from one player to another.
 *
 * @author Raivo Laanemets
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServerGamePrivateMessage extends ServerGameMessage {
	private Long fromId;
	private Long toId;
	private String text;
}
