package ee.pri.bcup.common.message.server.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Message that is sent when player who is actually taking
 * part of the game (not an observer) leaves the game.
 *
 * @author Raivo Laanemets
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServerGameExitMessage extends ServerGameMessage {
	private Long playerId;
}
