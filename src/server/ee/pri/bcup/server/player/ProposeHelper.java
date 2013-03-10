package ee.pri.bcup.server.player;

import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.MessageReceiver;
import ee.pri.bcup.common.message.client.propose.ClientProposeAcceptMessage;
import ee.pri.bcup.common.message.client.propose.ClientProposeCancelMessage;
import ee.pri.bcup.common.message.client.propose.ClientProposeMessage;
import ee.pri.bcup.common.message.client.propose.ClientProposeRejectMessage;
import ee.pri.bcup.server.context.ServerContext;
import ee.pri.bcup.server.context.ProposeManager.ProposeAccept;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Helper class for managing proposals.
 *
 * @author Raivo Laanemets
 */
public class ProposeHelper extends AbstractBaseHelper {

	public ProposeHelper(ServerPlayer player, ServerContext context) {
		super(player, context);
		
		player.getListenerSet().processListeners(this, ListenerScope.SERVER);
	}
	
	/**
	 * Current player accepts proposal.
	 */
	@MessageReceiver(ClientProposeAcceptMessage.class)
	public void receive(ClientProposeAcceptMessage message) {
		ProposeAccept accept = context.getProposeManager().acceptPropose(player);
		context.getRoomManager().start(accept.initiator, player, accept.hitTimeout, accept.gameType);
	}
	
	/**
	 * Current player rejects proposal.
	 */
	@MessageReceiver(ClientProposeRejectMessage.class)
	public void receive(ClientProposeRejectMessage message) {
		context.getProposeManager().rejectPropose(player);
	}
	
	/**
	 * Current player cancels proposal.
	 */
	@MessageReceiver(ClientProposeCancelMessage.class)
	public void receive(ClientProposeCancelMessage message) {
		context.getProposeManager().cancelPropose(player);
	}

	/**
	 * Current player proposes to another player.
	 */
	@MessageReceiver(ClientProposeMessage.class)
	public void receive(ClientProposeMessage message) {
		context.getProposeManager().propose(
			player,
			getPlayer(message.getPlayerIdTo()),
			message.getHitTimeout(),
			message.getGameType()
		);
	}
}
