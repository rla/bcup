package ee.pri.bcup.client.pool.model.hit;

import java.util.List;

import ee.pri.bcup.client.pool.model.PoolGame;
import ee.pri.bcup.common.model.pool.table.Ball;
import ee.pri.bcup.common.model.pool.table.BallType;

public class HitResult {
	private PoolGame context;
	private List<Ball> lastHoleBalls;
	private boolean whiteCollidedWithYellowFirst = false;;
	
	/**
	 * Constructs new hit result.
	 * 
	 * @param context game context
	 * @param lastHoleBalls list of balls that fell into holes during the last hit
	 * @param myBallsKind kind of ownership of our balls (can be left null or OTHER)
	 */
	public HitResult(PoolGame context) {
		
		this.context = context;
		this.lastHoleBalls = context.getTable().getBallsInHoles();
		
		checkWhiteCollidedWithYellowFirst();
	}
	
	private void checkWhiteCollidedWithYellowFirst() {
		Ball firstCollision = context.getTable().getWhiteBall().getFirstBallCollisionBeforeWallCollision();
		whiteCollidedWithYellowFirst = firstCollision != null && firstCollision.getType() == BallType.BALL1;
	}

	public boolean isWhiteCollidedWithYellowFirst() {
		return whiteCollidedWithYellowFirst;
	}

	public List<Ball> getLastHoleBalls() {
		return lastHoleBalls;
	}

}
