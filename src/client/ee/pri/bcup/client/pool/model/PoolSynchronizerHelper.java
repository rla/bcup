package ee.pri.bcup.client.pool.model;

import java.util.ArrayList;
import java.util.List;

import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.pool.ClientPoolBallsMessage;
import ee.pri.bcup.common.message.client.pool.ClientPoolEndMessage;
import ee.pri.bcup.common.message.server.game.ServerGameObserverMessage;
import ee.pri.bcup.common.message.server.pool.ServerPoolBallsMessage;
import ee.pri.bcup.common.model.GamePartyType;
import ee.pri.bcup.common.model.pool.table.Ball;
import ee.pri.bcup.common.model.pool.table.BallSpec;
import ee.pri.bcup.common.model.pool.table.HitState;
import ee.pri.bcup.common.model.pool.table.LocationSpec;
import ee.pri.bcup.common.model.pool.table.PoolGameSpec;
import ee.pri.bcup.common.model.pool.table.Table;

/**
 * Helper class for managing syncronization with observers.
 * Syncronization is only executed after the balls on table
 * have stopped and the game state on buddy has been calculated.
 * 
 * @author Raivo Laanemets
 */
public class PoolSynchronizerHelper {
	private PoolAppletContext appletContext;
	private List<Runnable> observerSyncronizationQueue = new ArrayList<Runnable>();
	
	public PoolSynchronizerHelper(final PoolAppletContext appletContext) {
		this.appletContext = appletContext;
		
		appletContext.processListeners(this, ListenerScope.GAME);
	}
	
	@MessageReceiver(ServerGameObserverMessage.class)
	public void receive(final ServerGameObserverMessage message) {
		
		if (appletContext.getProposeContext().isMyStart()
				&& appletContext.getGameContext().getPartyType() == GamePartyType.PLAYER) {
			
			if (appletContext.getGameContext().getHitState() == HitState.PLACE) {
				sendSynchronizationMessage(message.getPlayerId());
			} else {
				observerSyncronizationQueue.add(new Runnable() {
					@Override
					public void run() {
						sendSynchronizationMessage(message.getPlayerId());
					}
				});
			}
		}
		
		if (message.getPlayerId().equals(appletContext.getPlayer().getId())) {
			appletContext.getGameContext().getPoolFeedback().gameUserMessage(appletContext.getMessage("pool.field.message.sync"), false);
		}
	}
	
	@MessageReceiver(ServerPoolBallsMessage.class)
	public void receive(ServerPoolBallsMessage message) {
		applySpec(message.getSpec());
		appletContext.getGameContext().getPoolFeedback().hideMessage();		
	}
	
	/**
	 * This must be called after stop of balls. It will send syncronization
	 * message for all newly joined observers.
	 */
	public void runSynchronizers() {
		try {
			for (Runnable runnable : observerSyncronizationQueue) {
				runnable.run();
			}
		} finally {
			observerSyncronizationQueue = new ArrayList<Runnable>();
		}
	}
	
	private void applySpec(PoolGameSpec spec) {		
		PoolGame poolGame = appletContext.getGameContext();
		
		poolGame.setGameState(spec.getGameState());
		poolGame.setHitState(spec.getHitState());
		poolGame.setTurnState(spec.getTurnState());
		poolGame.setStartTries(spec.getStartTries());
		poolGame.setLeftBallsOwnership(spec.getOurBalls());
		poolGame.setHadWhite(spec.isHadWhite());
		
		Table table = poolGame.getTable();
		
		table.getWhiteBall().setOnTable(false);
		table.getWhiteBall().setVisible(false);
		
		for (BallSpec ballSpec : spec.getBalls()) {
			Ball ball = table.lookupBall(ballSpec.getType());
			
			if (ball == null) {
				continue;
			}
			
			ball.setLocation(ballSpec.getLocation().toDoubleTuple());

			if (ballSpec.isMine()) {
				ball.setOnTable(false);
				ball.setVisible(false);
				poolGame.getLeft().getBalls().add(ball);
			}
			
			if (ballSpec.isOpponents()) {
				ball.setOnTable(false);
				ball.setVisible(false);
				poolGame.getRight().getBalls().add(ball);
			}
		}
		
		poolGame.getHitTimer().start(spec.getHitTimeLeft().intValue());
	}

	private void sendSynchronizationMessage(Long playerId) {
		PoolGame poolGame = appletContext.getGameContext();
		
		// Handles game end case separatedly
		if (poolGame.getWinner() != null) {
			ClientPoolEndMessage message = new ClientPoolEndMessage();
			message.setWinnerPlayerId(poolGame.getWinner().getId());
			message.setPlayerId(playerId);
			appletContext.send(message);
		} else {
			appletContext.send(new ClientPoolBallsMessage(playerId, createCurrentSpec()));
		}
	}
	
	private PoolGameSpec createCurrentSpec() {
		PoolGameSpec gameSpec = new PoolGameSpec();
		PoolGame poolGame = appletContext.getGameContext();
		
		List<BallSpec> balls = new ArrayList<BallSpec>();
		List<Ball> myBalls = poolGame.getLeft().getBalls();
		List<Ball> opponentBalls = poolGame.getRight().getBalls();
		
		for (Ball ball : poolGame.getTable().getBalls()) {
			BallSpec spec = new BallSpec();
			spec.setLocation(LocationSpec.fromDoubleTuple(ball.getLocation()));
			spec.setType(ball.getType());
			spec.setMine(myBalls.contains(ball));
			spec.setOpponents(opponentBalls.contains(ball));
			balls.add(spec);
		}
		
		gameSpec.setBalls(balls.toArray(new BallSpec[0]));
		gameSpec.setGameState(poolGame.getGameState());
		gameSpec.setHadWhite(poolGame.isHadWhite());
		gameSpec.setHitState(poolGame.getHitState());
		gameSpec.setOurBalls(poolGame.getLeftBallsOwnership());
		gameSpec.setStartTries(poolGame.getStartTries());
		gameSpec.setTurnState(poolGame.getTurnState());
		gameSpec.setHitTimeLeft(poolGame.getHitTimer().getRemainingTime());
		
		return gameSpec;
	}
	
}
