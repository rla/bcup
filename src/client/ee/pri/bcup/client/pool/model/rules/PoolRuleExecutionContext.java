package ee.pri.bcup.client.pool.model.rules;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.model.PoolGame;
import ee.pri.bcup.client.pool.model.hit.HitResult;
import ee.pri.bcup.common.model.Player;
import ee.pri.bcup.common.model.pool.table.Ball;
import ee.pri.bcup.common.model.pool.table.BallOwnershipKind;
import ee.pri.bcup.common.model.pool.table.GameState;
import ee.pri.bcup.common.model.pool.table.HitState;
import ee.pri.bcup.common.model.pool.table.TurnState;

@Data
public class PoolRuleExecutionContext {
	private final PoolAppletContext appletContext;
	private final HitResult hitResult;
	private Player winner;
	private List<Ball> lastHoleBalls;
	private List<Ball> leftBalls = new ArrayList<Ball>();
	private List<Ball> rightBalls = new ArrayList<Ball>();
	private Ball firstOwnedBall;
	private boolean black = false;
	private boolean white = false;
	private GameState gameState;
	private TurnState turnState;
	private HitState hitState;
	
	/**
	 * Helper method for executing rules. Divides balls
	 * by given ownership information.
	 * 
	 * @param leftOwn ownership type of left player.
	 */
	public void divideBalls(BallOwnershipKind leftOwn) {
		for (Ball ball : lastHoleBalls) {
			if (ball.getType().getKind() == leftOwn) {
				leftBalls.add(ball);
			}
			if (ball.getType().getKind() == leftOwn.getOpposite()) {
				rightBalls.add(ball);
			}
		}
	}
	
	/**
	 * Helper method for executing rules.
	 */
	public void resoveOwnership(Ball ownedFirst) {
		PoolGame game = appletContext.getGameContext();
		
		if (game.getTurnState() == TurnState.LEFT) {
			game.setLeftBallsOwnership(ownedFirst.getType().getKind());
		} else {
			game.setLeftBallsOwnership(ownedFirst.getType().getKind().getOpposite());
		}
	}
}
