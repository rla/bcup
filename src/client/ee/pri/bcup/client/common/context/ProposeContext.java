package ee.pri.bcup.client.common.context;

import java.io.Serializable;

import org.apache.log4j.Logger;

import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.propose.ClientProposeAcceptMessage;
import ee.pri.bcup.common.message.client.propose.ClientProposeCancelMessage;
import ee.pri.bcup.common.message.client.propose.ClientProposeMessage;
import ee.pri.bcup.common.message.client.propose.ClientProposeRejectMessage;
import ee.pri.bcup.common.message.server.ServerPartRoomMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeAcceptMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeByOtherMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeCancelByOtherMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeFailMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeRejectMessage;
import ee.pri.bcup.common.model.GameType;
import ee.pri.bcup.common.model.Player;
import ee.pri.bcup.common.model.ProposeRole;

/**
 * Helper class for handling game proposes.
 *
 * @author Raivo Laanemets
 */
public class ProposeContext implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(ProposeContext.class);
	
	private boolean proposeActive = false;
	private ProposeRole proposeRole;
	private Player proposePartner;
	private AppletContext appletContext;
	private Long hitTimeout = 20L;
	private GameType gameType = GameType.POOL_8BALL;
	
	public ProposeContext(AppletContext appletContext) {
		this.appletContext = appletContext;
		
		appletContext.processListeners(this, ListenerScope.APPLET);
	}
	
	/**
	 * This method is called when other player proposes
	 * to the current player.
	 */
	@MessageReceiver(ServerProposeByOtherMessage.class)
	public void receive(ServerProposeByOtherMessage message) {
		Long id = message.getPlayerIdFrom();
		
		proposePartner = appletContext.getPlayer(id);
		proposeActive = true;
		proposeRole = ProposeRole.TARGET;
		hitTimeout = message.getHitTimeout();
		gameType = message.getGameType();
		
		log.debug("propose by " + proposePartner);
	}
	
	/**
	 * This is called when the other player that i'm currently proposing,
	 * rejects the proposal.
	 */
	@MessageReceiver(ServerProposeRejectMessage.class)
	public void receive(ServerProposeRejectMessage message) {
		proposeActive = false;
	}
	
	@MessageReceiver(ServerProposeCancelByOtherMessage.class)
	public void receive(ServerProposeCancelByOtherMessage message) {
		proposeActive = false;
	}
	
	@MessageReceiver(ServerProposeAcceptMessage.class)
	public void receive(ServerProposeAcceptMessage message) {
		proposeActive = false;
	}
	
	@MessageReceiver(ServerProposeFailMessage.class)
	public void receive(ServerProposeFailMessage message) {
		proposeActive = false;
	}
	
	@MessageReceiver(ServerPartRoomMessage.class)
	public void receive(ServerPartRoomMessage message) {
		if (proposeActive
				&& message.getPlayerId().equals(proposePartner.getId())) {
			
			proposeActive = false;
		}
	}
	
	/**
	 * Returns the player that we are proposing
	 * to or proposed by. This will return null if no propose
	 * action involving us has taken part.
	 */
	public Player getProposePartner() {
		return proposePartner;
	}
	
	public boolean isProposeActive() {
		return proposeActive;
	}
	
	/**
	 * Called when we propose to another player.
	 */
	public void proposeTo(Player player, Long hitTimeout, GameType gameType) {
		proposePartner = player;
		proposeActive = true;
		proposeRole = ProposeRole.INITIATOR;
		this.hitTimeout = hitTimeout;
		this.gameType = gameType;
		
		log.debug("proposing to " + proposePartner + " with hit timeout " + hitTimeout);
		
		ClientProposeMessage message = new ClientProposeMessage();
		message.setPlayerIdTo(player.getId());
		message.setHitTimeout(hitTimeout);
		message.setGameType(gameType);
		
		appletContext.send(message);
	}
	
	/**
	 * Cancels propose action.
	 */
	public void cancelPropose() {
		proposeActive = false;
		if (proposeRole == ProposeRole.INITIATOR) {
			appletContext.send(new ClientProposeCancelMessage());
		}
	}
	
	/**
	 * Accepts proposal.
	 */
	public void acceptPropose() {
		log.debug("accepting propose");
		proposeActive = false;
		if (proposeRole == ProposeRole.TARGET) {
			appletContext.send(new ClientProposeAcceptMessage());
			appletContext.acceptGame();
		}
	}
	
	/**
	 * Rejects proposal.
	 */
	public void rejectPropose() {
		proposeActive = false;
		if (proposeRole == ProposeRole.TARGET) {
			appletContext.send(new ClientProposeRejectMessage());
		}
	}
	
	/**
	 * Returns whether the game should start with my turn.
	 * Currently it returns true if we started game.
	 */
	public boolean isMyStart() {
		return proposeRole == ProposeRole.INITIATOR;
	}

	public Long getHitTimeout() {
		return hitTimeout;
	}

	public GameType getGameType() {
		return gameType;
	}

}
