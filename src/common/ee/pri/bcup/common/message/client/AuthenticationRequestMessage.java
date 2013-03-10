package ee.pri.bcup.common.message.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthenticationRequestMessage extends ClientMessage {
	private Long userId;
	private String sessionKey;
	private String databaseEnvKey;
	private Integer version;
	
	public AuthenticationRequestMessage() {}
	
	public AuthenticationRequestMessage(Long userId, String sessionKey, String databaseEnvKey, Integer version) {
		this.userId = userId;
		this.sessionKey = sessionKey;
		this.databaseEnvKey = databaseEnvKey;
		this.version = version;
	}

}
