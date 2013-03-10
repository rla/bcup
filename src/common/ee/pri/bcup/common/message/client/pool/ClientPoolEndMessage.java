package ee.pri.bcup.common.message.client.pool;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientPoolEndMessage extends ClientPoolMessage {
	private Long winnerPlayerId;
	private Long playerId;
}
