package ee.pri.bcup.common.message.server.propose;

import ee.pri.bcup.common.message.server.ServerMessage;
import ee.pri.bcup.common.model.GameType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerProposeByOtherMessage extends ServerMessage {
	private Long playerIdFrom;
	private Long hitTimeout;
	private GameType gameType;
	
	public ServerProposeByOtherMessage() {}

	public ServerProposeByOtherMessage(Long playedIdFrom) {
		this.playerIdFrom = playedIdFrom;
	}

}
