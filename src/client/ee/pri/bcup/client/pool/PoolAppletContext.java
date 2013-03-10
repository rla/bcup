package ee.pri.bcup.client.pool;

import org.apache.log4j.Logger;

import ee.pri.bcup.client.common.BCupApplet;
import ee.pri.bcup.client.common.ClientPlayer;
import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.pool.model.PoolGame;
import ee.pri.bcup.client.pool.model.PoolSynchronizerHelper;
import ee.pri.bcup.common.model.GamePartyType;
import ee.pri.bcup.common.model.GameType;

/**
 * Applet context for pool game.
 * 
 * @author Raivo Laanemets
 */
public class PoolAppletContext extends AppletContext {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(PoolAppletContext.class);
	
	public static final int FIELD_WIDTH = 620;
	public static final int FIELD_HEIGHT = 330;
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 589;
	
	private PoolGame gameContext;
	private PoolObserverContext observerContext;

	public PoolAppletContext(BCupApplet applet, ClientPlayer player) {
		super(applet, player);
	}

	public PoolGame getGameContext() {
		return gameContext;
	}
	
	/**
	 * Creates new pool game context.
	 * 
	 * @param partyType type of this party (player or observer).
	 */
	public void createGameContext(GamePartyType partyType) {
		log.debug("creating game context");
		
		Long hitTimeout = partyType == GamePartyType.OBSERVER
				? getObserverContext().getHitTimeout()
				: getProposeContext().getHitTimeout();
				
		GameType gameType = partyType == GamePartyType.OBSERVER
				? getObserverContext().getGameType()
				: getProposeContext().getGameType();
		
		gameContext = new PoolGame(this, partyType, hitTimeout, getProposeContext().isMyStart(), gameType);
		gameContext.setSynchronizerHelper(new PoolSynchronizerHelper(this));
	}
	
	public int getCanvasWidth() {
		return FIELD_WIDTH;
	}
	
	public int getCanvasHeight() {
		return FIELD_HEIGHT;
	}

	@Override
	public void destroyGameContext() {
		if (gameContext != null) {
			log.debug("destroying pool context");
			gameContext.destroy();
			gameContext = null;
		}
	}

	public PoolObserverContext getObserverContext() {
		return observerContext;
	}

	public void setObserverContext(PoolObserverContext observerContext) {
		this.observerContext = observerContext;
	}

}
