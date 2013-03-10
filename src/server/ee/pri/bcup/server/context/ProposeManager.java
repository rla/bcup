package ee.pri.bcup.server.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import ee.pri.bcup.common.message.server.propose.ServerProposeAcceptMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeByOtherMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeCancelByOtherMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeFailMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeRejectMessage;
import ee.pri.bcup.common.message.server.propose.ServerProposeSuccessMessage;
import ee.pri.bcup.common.model.GameType;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for managing game proposals.
 *
 * @author Raivo Laanemets
 */
public class ProposeManager implements DisconnectListener, InitializingBean, DisposableBean {
	private static final Logger log = Logger.getLogger(ProposeManager.class);
	
	private Map<ServerPlayer, ServerPlayer> proposeInfo =
		new ConcurrentHashMap<ServerPlayer, ServerPlayer>();
	
	private Map<ServerPlayer, Long> hitTimeouts =
		new ConcurrentHashMap<ServerPlayer, Long>();
	
	private Map<ServerPlayer, GameType> gameTypes =
		new ConcurrentHashMap<ServerPlayer, GameType>();

	/**
	 * Creates new propose from initiator to target.
	 */
	public void propose(ServerPlayer initiator, ServerPlayer target, Long hitTimeout, GameType gameType) {		
		
		if (!proposeInfo.containsKey(initiator)
				&& !proposeInfo.containsKey(target)) {
			
			log.debug("creating propose between players "
					+ initiator + " and " + target);
			
			proposeInfo.put(initiator, target);
			proposeInfo.put(target, initiator);
			
			initiator.send(new ServerProposeSuccessMessage());
			
			ServerProposeByOtherMessage message = new ServerProposeByOtherMessage();
			
			message.setPlayerIdFrom(initiator.getId());
			message.setHitTimeout(hitTimeout);
			message.setGameType(gameType);
			
			hitTimeouts.put(initiator, hitTimeout);
			gameTypes.put(initiator, gameType);
			
			target.send(message);
		} else {
			initiator.send(new ServerProposeFailMessage());
		}
	}
	
	/**
	 * Cancels proposal by game initiator.
	 */
	public void cancelPropose(ServerPlayer initiator) {
		ServerPlayer partner = proposeInfo.get(initiator);
		log.debug("player " + initiator + " cancels proposal to " + partner);
		partner.send(new ServerProposeCancelByOtherMessage());
		removePropose(initiator);
	}
	
	/**
	 * Rejects proposal by target.
	 */
	public void rejectPropose(ServerPlayer target) {
		ServerPlayer partner = proposeInfo.get(target);
		log.debug("player " + target + " rejects proposal by " + partner);
		partner.send(new ServerProposeRejectMessage());
		removePropose(target);
	}
	
	/**
	 * Accepts proposal by target. Returns true if
	 * the accept was successful and a game should
	 * be starter.
	 * 
	 * Returns the initiator.
	 */
	public ProposeAccept acceptPropose(ServerPlayer target) {
		ServerPlayer partner = proposeInfo.get(target);
		Long hitTimeout = hitTimeouts.get(partner);
		
		log.debug("player " + target+ " accepts proposal by " + partner);
		partner.send(new ServerProposeAcceptMessage());
		removePropose(target);
		
		return new ProposeAccept(partner, hitTimeout, gameTypes.get(partner));
	}
	
	public static class ProposeAccept {
		public final ServerPlayer initiator;
		public final Long hitTimeout;
		public final GameType gameType;
		
		public ProposeAccept(ServerPlayer initiator, Long hitTimeout, GameType gameType) {
			this.initiator = initiator;
			this.hitTimeout = hitTimeout;
			this.gameType = gameType;
		}
	
	}
	
	@Override
	public void disconnected(ServerPlayer player) {
		removePropose(player);
	}
	
	private void removePropose(ServerPlayer player) {
		ServerPlayer partner = proposeInfo.get(player);
		
		if (partner == null) {
			return;
		}
		
		log.debug("removing propose between players "
				+ player + " and " + partner);
			
		proposeInfo.remove(player);
		proposeInfo.remove(partner);
		
		hitTimeouts.remove(player);
		hitTimeouts.remove(partner);
		
		gameTypes.remove(player);
		gameTypes.remove(partner);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("propose manager created");
	}

	@Override
	public void destroy() throws Exception {
		log.debug("propose manager destroyed");
	}
	
}
