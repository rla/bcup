package ee.pri.bcup.common.message.client.propose;

import ee.pri.bcup.common.message.client.ClientMessage;
import ee.pri.bcup.common.model.GameType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientProposeMessage extends ClientMessage {
	private Long playerIdTo;
	private Long hitTimeout;
	private GameType gameType;
}
