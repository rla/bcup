package ee.pri.bcup.common.message.server.pool;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Message to be sent to joining observer when one
 * of the players have already won.
 *
 * @author Raivo Laanemets
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServerPoolEndMessage extends ServerPoolMessage {
	private Long winnerPlayerId;
}
