package ee.pri.bcup.common.message.client.pool;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ee.pri.bcup.common.model.pool.table.PoolGameSpec;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientPoolBallsMessage extends ClientPoolMessage {
	private Long playerId;
	private PoolGameSpec spec;
	
	public ClientPoolBallsMessage() {}
	
	public ClientPoolBallsMessage(Long playerId, PoolGameSpec spec) {
		this.playerId = playerId;
		this.spec = spec;
	}

}
