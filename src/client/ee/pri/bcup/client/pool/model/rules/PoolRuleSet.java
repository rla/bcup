package ee.pri.bcup.client.pool.model.rules;

import java.util.Collections;

import ee.pri.bcup.client.common.log.Logger;
import ee.pri.bcup.client.pool.PoolAppletContext;
import ee.pri.bcup.client.pool.model.PoolGame;
import ee.pri.bcup.client.pool.model.hit.HitResult;
import ee.pri.bcup.client.pool.model.rules.xml.XmlRuleEvaluator;
import ee.pri.bcup.common.message.client.game.ClientGameLoseMessage;
import ee.pri.bcup.common.model.GamePartyType;
import ee.pri.bcup.common.model.pool.table.Ball;

public class PoolRuleSet {
	private static final Logger log = Logger.getLogger(PoolRuleSet.class);
	
	private XmlRuleEvaluator evaluator;
	
	public PoolRuleSet() {
		evaluator = new XmlRuleEvaluator();
	}
	
	/**
	 * Executes the game rules.
	 * 
	 * @param appletContext the applet context
	 * @param hitResult result of last hit (might be null if there was timeout)
	 */
	public void execute(PoolAppletContext appletContext, HitResult hitResult) {
		log.debug("executing pool rules");
		
		PoolGame poolGame = appletContext.getGameContext();
		
		PoolRuleExecutionContext executionContext = new PoolRuleExecutionContext(appletContext, hitResult);
		executionContext.setLastHoleBalls(hitResult == null ? Collections.<Ball>emptyList() : hitResult.getLastHoleBalls());
		executionContext.setGameState(poolGame.getGameState());
		executionContext.setTurnState(poolGame.getTurnState());
		executionContext.setHitState(poolGame.getHitState());
		
		precalculateBalls(executionContext);
		
		evaluator.evaluate(executionContext, appletContext, hitResult);
		
		log.debug(appletContext.getPlayer() + ": result of rules " + executionContext);
		
		poolGame.setGameState(executionContext.getGameState());
		poolGame.setTurnState(executionContext.getTurnState());
		poolGame.setHitState(executionContext.getHitState());
		poolGame.setHadWhite(executionContext.isWhite());
		poolGame.setWinner(executionContext.getWinner());
		
		for (Ball ball : executionContext.getLeftBalls()) {
			poolGame.getLeft().getBalls().add(ball);
		}
		
		for (Ball ball : executionContext.getRightBalls()) {
			poolGame.getRight().getBalls().add(ball);
		}
		
		poolGame.getPoolFeedback().statusChanged();
		if (executionContext.getWinner() != null) {
			poolGame.getHitTimer().stop();
			
			poolGame.getPoolFeedback().gameUserMessage(
				appletContext.getMessage("pool.field.message.won.other",
				executionContext.getWinner()
			), true);
			
			if (poolGame.getPartyType() == GamePartyType.PLAYER
				&& !executionContext.getWinner().equals(appletContext.getPlayer())) {
				
				appletContext.send(new ClientGameLoseMessage());
			}
		} else {
			poolGame.getHitTimer().start();
		}
	}
	
	private void precalculateBalls(PoolRuleExecutionContext executionContext) {
		boolean white = false;
		boolean black = false;
		Ball firstOwnedBall = null;
		
		for (Ball ball : executionContext.getLastHoleBalls()) {
			if (ball.getType().isBlack()) {
				black = true;
			}
			
			if (ball.getType().isWhite()) {
				white = true;
			}
			
			if (firstOwnedBall == null && ball.getType().getKind() != null) {
				firstOwnedBall = ball;
			}
		}
		
		executionContext.setBlack(black);
		executionContext.setWhite(white);
		executionContext.setFirstOwnedBall(firstOwnedBall);
	}
}
