package ee.pri.bcup.common.message.server.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Server sends this message to all parties of
 * the game when some observer leaves.
 *
 * @author Raivo Laanemets
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServerGameObserverExitMessage extends ServerGameMessage {
	private Long playerId;
}
