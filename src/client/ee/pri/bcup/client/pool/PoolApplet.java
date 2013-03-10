package ee.pri.bcup.client.pool;

import java.io.IOException;

import org.apache.log4j.Logger;

import ee.pri.bcup.client.common.BCupApplet;
import ee.pri.bcup.client.common.ClientPlayer;
import ee.pri.bcup.client.common.context.AppletContext;
import ee.pri.bcup.client.pool.field.PoolFieldPanel;
import ee.pri.bcup.common.model.GamePartyType;

public class PoolApplet extends BCupApplet {
	private static final Logger log = Logger.getLogger(PoolApplet.class);
	
	private static final long serialVersionUID = 1L;

	// FIXME latency correction is not used
	@Override
	public void startGame(GamePartyType partyType, long latencyCorrection) {
		log.debug("starting new game");
		
		try {
			PoolAppletContext appletContext = (PoolAppletContext) getContext();
			appletContext.createGameContext(partyType);
			
			fieldPanel = new PoolFieldPanel(appletContext);
			setPanel(fieldPanel);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	protected AppletContext createContext(ClientPlayer player) {
		return new PoolAppletContext(this, player);
	}
}
