package ee.pri.bcup.common.message.server;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerPartRoomMessage extends ServerMessage {
	private Long playerId;
	
	public ServerPartRoomMessage() {}

	public ServerPartRoomMessage(Long playerId) {
		this.playerId = playerId;
	}

}
