package ee.pri.bcup.client.pool;

import java.io.Serializable;

import ee.pri.bcup.client.common.log.Logger;
import ee.pri.bcup.common.model.GameType;
import ee.pri.bcup.common.model.Player;

/**
 * Helper context class for use when observing a pool game.
 * 
 * @author Raivo Laanemets
 */
public class PoolObserverContext implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(PoolObserverContext.class);
	
	private Player buddy;
	private Player opponent;
	private Long hitTimeout;
	private GameType gameType;
	
	public PoolObserverContext(Player buddy, Player opponent, Long hitTimeout, GameType gameType) {
		log.debug("buddy: " + buddy + ", opponent " + opponent);
		
		this.buddy = buddy;
		this.opponent = opponent;
		this.hitTimeout = hitTimeout;
		this.gameType = gameType;
	}

	public Player getBuddy() {
		return buddy;
	}

	public Player getOpponent() {
		return opponent;
	}

	public Long getHitTimeout() {
		return hitTimeout;
	}

	public GameType getGameType() {
		return gameType;
	}
	
}
