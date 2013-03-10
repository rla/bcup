package ee.pri.bcup.client.pool.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;

import org.apache.log4j.Logger;

import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.model.hit.HitResult;
import ee.pri.bcup.client.pool.model.hit.HitTimer;
import ee.pri.bcup.client.pool.model.listener.PoolFeedback;
import ee.pri.bcup.client.pool.model.rules.PoolRuleSet;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.server.pool.ServerPoolEndMessage;
import ee.pri.bcup.common.message.server.pool.ServerPoolSpeedMessage;
import ee.pri.bcup.common.model.GamePartyType;
import ee.pri.bcup.common.model.GameType;
import ee.pri.bcup.common.model.Player;
import ee.pri.bcup.common.model.pool.table.Ball;
import ee.pri.bcup.common.model.pool.table.BallOwnershipKind;
import ee.pri.bcup.common.model.pool.table.DoubleTuple;
import ee.pri.bcup.common.model.pool.table.GameState;
import ee.pri.bcup.common.model.pool.table.HitState;
import ee.pri.bcup.common.model.pool.table.Table;
import ee.pri.bcup.common.model.pool.table.TurnState;

@Data
public class PoolGame implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(PoolGame.class);

	private Table table;
	
	private GameState gameState = GameState.START_HIT;
	private HitState hitState = HitState.PLACE;
	private TurnState turnState = TurnState.LEFT;
	private GamePartyType partyType;
	
	private int startTries = 0;
	private BallOwnershipKind leftBallsOwnership;
	
	private PoolAppletContext appletContext;
	private HitTimer hitTimer;
	private Animator animator;
	
	private PoolFeedback poolFeedback;
	
	private boolean hadWhite = false;
	private boolean hitTimeout = false;
	
	private PoolSynchronizerHelper synchronizerHelper;
	private Player winner;
	private PoolRuleSet poolRuleSet = new PoolRuleSet();
	
	private Player left;
	private Player right;
	
	private SpinHint spinHint = new SpinHint();

	public PoolGame(
			final PoolAppletContext appletContext,
			final GamePartyType partyType,
			long maxTimeInSeconds,
			boolean myTurn,
			GameType gameType) {
		
		log.debug("creating pool game");
		
		this.appletContext = appletContext;
		this.partyType = partyType;
		this.table = new Table(gameType);
		this.poolFeedback = new PoolFeedback();
		this.hitTimer = new HitTimer(this, maxTimeInSeconds);
		this.hitTimer.start();
		this.animator = new Animator(this);
		
		turnState = myTurn ? TurnState.LEFT : TurnState.RIGHT;
		
		if (partyType == GamePartyType.OBSERVER) {
			left = appletContext.getObserverContext().getBuddy();
			right = appletContext.getObserverContext().getOpponent();
		} else {
			left = appletContext.getPlayer();
			right = appletContext.getProposeContext().getProposePartner();
		}
		
		left.setBalls(new ArrayList<Ball>());
		right.setBalls(new ArrayList<Ball>());
		
		appletContext.processListeners(this, ListenerScope.GAME);
	}
	
	@MessageReceiver(ServerPoolSpeedMessage.class)
	public void receive(ServerPoolSpeedMessage message) {
		if (getTurnState() == TurnState.RIGHT || partyType == GamePartyType.OBSERVER) {
			setHitState(HitState.OTHER);
			
			Ball whiteBall = table.getWhiteBall();
			
			whiteBall.setLocation(new DoubleTuple(message.getLocationX(), message.getLocationY()));
			whiteBall.setSpeed(new DoubleTuple(message.getSpeedX(), message.getSpeedY()));
			whiteBall.setHorisontalSpin(message.getHorisontalSpin());
			whiteBall.setVerticalSpin(message.getVerticalSpin());
			
			hit();
		}
	}
	
	@MessageReceiver(ServerPoolEndMessage.class)
	public void receive(ServerPoolEndMessage message) {
		Player player = appletContext.getPlayer(message.getWinnerPlayerId());
		hitTimer.stop();
		poolFeedback.gameUserMessage(appletContext.getMessage("pool.field.message.won.other", player), true);
	}
	
	public void resetTable() {
		table.reset();
		left.setBalls(new ArrayList<Ball>());
		right.setBalls(new ArrayList<Ball>());
	}

	public void setHitState(HitState hitState) {
		this.hitState = hitState;
	}
	
	public void hit() {
		table.getWhiteBall().setOnTable(true);
		table.getWhiteBall().setVisible(true);
		poolFeedback.hit();
		animator.hit();
		table.cleanHoles();
		hitTimer.stop();
	}

	public boolean isAllLeftBallsIn() {
		if (leftBallsOwnership == null) {
			return false;
		}
		for (Ball ball : table.getBalls()) {
			if (ball.getType().getKind() == leftBallsOwnership  && ball.isOnTable()) {
				return false;
			}
		}
		
		return true;
	}

	public boolean isAllRightBallsIn() {
		if (leftBallsOwnership == null) {
			return false;
		}
		for (Ball ball : table.getBalls()) {
			if (ball.getType().getKind() == leftBallsOwnership.getOpposite()  && ball.isOnTable()) {
				return false;
			}
		}
		
		return true;
	}
	
	public void confirmUserMessage() {
		if (gameState == GameState.END) {
			appletContext.exitGame();
		}
	}

	/**
	 * Called by {@link HitTimer} when the hit timeout has occurred.
	 */
	public void hitTimeoutOccurred() {
		hitTimeout = true;
		poolRuleSet.execute(appletContext, null);
	}
	
	/**
	 * Called by {@link Animator} when balls have stopped
	 * moving after hit. 
	 */
	public void ballsStopped() {
		HitResult hitResult = new HitResult(this);
		poolRuleSet.execute(appletContext, hitResult);
		synchronizerHelper.runSynchronizers();
	}
	
	public BallOwnershipKind resolveOwenership(Ball ball) {
		if (left.getBalls().contains(ball)) {
			return leftBallsOwnership;
		} else if (right.getBalls().contains(ball)) {
			return leftBallsOwnership.getOpposite();
		}
		
		return null;
	}
	
	/**
	 * Used in game rules.
	 */
	public boolean isBallOwnershipDecided() {
		return leftBallsOwnership != null;
	}
	
	public void destroy() {
		hitTimer.stop();
		animator.stop();
	}
}
