package ee.pri.bcup.common.message.server;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AuthenticationSuccessResponseMessage extends ServerMessage {
	private String name;
	private Long id;
	
	public AuthenticationSuccessResponseMessage() {}
	
	public AuthenticationSuccessResponseMessage(String name, Long id) {
		this.name = name;
		this.id = id;
	}

}
