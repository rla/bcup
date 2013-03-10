package ee.pri.bcup.common.message.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Message that is sent by special player that issues
 * shutdown of the server.
 * 
 * @author Raivo Laanemets
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ClientShutdownMessage extends ClientMessage {
	private String magicWord;
	
	public ClientShutdownMessage() {}

	public ClientShutdownMessage(String magicWord) {
		this.magicWord = magicWord;
	}

}
