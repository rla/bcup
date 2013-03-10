package ee.pri.bcup.common.message.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientRoomMessage extends ClientMessage {
	private String message;
	
	public ClientRoomMessage() {}

	public ClientRoomMessage(String message) {
		this.message = message;
	}

}
