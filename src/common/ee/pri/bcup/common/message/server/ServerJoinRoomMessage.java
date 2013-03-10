package ee.pri.bcup.common.message.server;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerJoinRoomMessage extends ServerMessage {
	private Long playerId;
	private String name;
	
	public ServerJoinRoomMessage() {}

	public ServerJoinRoomMessage(String name, Long playerId) {
		this.name = name;
		this.playerId = playerId;
	}

}
