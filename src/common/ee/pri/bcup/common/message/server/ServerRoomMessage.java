package ee.pri.bcup.common.message.server;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerRoomMessage extends ServerMessage {
	private String message;
	private Long senderId;
	
	public ServerRoomMessage() {}
	
	public ServerRoomMessage(String message, Long senderId) {
		this.message = message;
		this.senderId = senderId;
	}
	
}
