package ee.pri.bcup.common.message.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientJoinRoomMessage extends ClientMessage {
	private Long roomId;
	
	public ClientJoinRoomMessage() {}
	
	public ClientJoinRoomMessage(Long roomId) {
		this.roomId = roomId;
	}

}
