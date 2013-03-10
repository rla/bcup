package ee.pri.bcup.common.message.client.game;

import ee.pri.bcup.common.message.client.ClientMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientGameObserveMessage extends ClientMessage {
	private Long gameId;
	
	public ClientGameObserveMessage() {}
	
	public ClientGameObserveMessage(Long gameId) {
		this.gameId = gameId;
	}

}
