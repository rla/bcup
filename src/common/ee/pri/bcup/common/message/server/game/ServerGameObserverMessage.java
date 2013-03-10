package ee.pri.bcup.common.message.server.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerGameObserverMessage extends ServerGameMessage {
	private Long playerId;
	
	public ServerGameObserverMessage() {}

	public ServerGameObserverMessage(Long playerId) {
		this.playerId = playerId;
	}
	
}
