package ee.pri.bcup.common.message.client.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientGameTextMessage extends ClientGameMessage {
	private String message;
	
	public ClientGameTextMessage() {}
	
	public ClientGameTextMessage(String message) {
		this.message = message;
	}

}
