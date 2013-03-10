package ee.pri.bcup.common.message.server.pool;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServerPoolSpeedMessage extends ServerPoolMessage {
	private Double locationX;
	private Double locationY;
	private Double speedX;
	private Double speedY;
	private Double horisontalSpin;
	private Double verticalSpin;
	
	public ServerPoolSpeedMessage() {}
	
	public ServerPoolSpeedMessage(
			Double locationX,
			Double locationY,
			Double speedX,
			Double speedY,
			Double horisontalSpin,
			Double verticalSpin) {
				
		this.locationX = locationX;
		this.locationY = locationY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.horisontalSpin = horisontalSpin;
		this.verticalSpin = verticalSpin;
	}
	
}
