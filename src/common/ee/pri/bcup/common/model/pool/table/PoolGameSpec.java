package ee.pri.bcup.common.model.pool.table;

import lombok.Data;

@Data
public class PoolGameSpec {
	private BallSpec[] balls;
	
	private GameState gameState = GameState.START_HIT;
	private HitState hitState = HitState.PLACE;
	private TurnState turnState = TurnState.LEFT;
	
	private Integer startTries = 0;
	private Integer opponentStartTries = 0;
	
	private BallOwnershipKind ourBalls;
	private boolean hadWhite = false;
	
	private Long hitTimeLeft;

}
