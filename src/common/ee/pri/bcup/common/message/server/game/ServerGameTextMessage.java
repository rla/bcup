package ee.pri.bcup.common.message.server.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerGameTextMessage extends ServerGameMessage {
	private Long fromId;
	private String message;
	
	public ServerGameTextMessage() {}
	
	public ServerGameTextMessage(Long fromId, String message) {
		this.fromId = fromId;
		this.message = message;
	}

}
