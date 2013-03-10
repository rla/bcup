package ee.pri.bcup.common.message.server.pool;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ee.pri.bcup.common.model.pool.table.PoolGameSpec;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerPoolBallsMessage extends ServerPoolMessage {
	private PoolGameSpec spec;
	
	public ServerPoolBallsMessage() {}
	
	public ServerPoolBallsMessage(PoolGameSpec spec) {
		this.spec = spec;
	}

}
