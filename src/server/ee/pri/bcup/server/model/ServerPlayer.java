package ee.pri.bcup.server.model;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import ee.pri.bcup.common.message.ListenerScope;
import ee.pri.bcup.common.message.ListenerSet;
import ee.pri.bcup.common.message.Message;
import ee.pri.bcup.common.message.client.ClientPingMessage;
import ee.pri.bcup.common.message.server.ServerPingMessage;
import ee.pri.bcup.common.net.NetPlayer;
import ee.pri.bcup.server.context.ServerContext;
import ee.pri.bcup.server.dao.Entity;
import ee.pri.bcup.server.player.GameHelper;
import ee.pri.bcup.server.player.PoolHelper;
import ee.pri.bcup.server.player.ProposeHelper;
import ee.pri.bcup.server.player.RoomHelper;
import ee.pri.bcup.server.player.ServerHelper;

/**
 * Represents one player on the server side. All logics
 * is handled here and helper classes.
 */
public class ServerPlayer extends NetPlayer implements Entity {
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(ServerPlayer.class);
	
	private ServerContext serverContext;
	private long clientLatency;
	private long lastPingTime;
	private ListenerSet listenerSet = new ListenerSet();
	private ServerRoom room;
	private ServerGame game;
	private boolean disconnected = false;
	
	public ServerPlayer(
			Socket socket,
			ServerContext serverContext,
			int clientTimeout) throws IOException {
		
		super(socket, clientTimeout);
		this.serverContext = serverContext;
		
		new ServerHelper(this, serverContext);
		new GameHelper(this, serverContext);
		new PoolHelper(this, serverContext);
		new ProposeHelper(this, serverContext);
		new RoomHelper(this, serverContext);
		
		listenerSet.processListeners(this, ListenerScope.SERVER);
	}

	@Override
	public void handleDisconnect() {
		serverContext.getPlayerManager().disconnect(this);
	}

	@Override
	protected void receiveMessage(Message message) {
		if (!(message instanceof ClientPingMessage)) {
			log.debug(this + " receives message: " + message);
		}
		listenerSet.message(message);
	}

	@Override
	public void send(Message message) {
		if (!(message instanceof ServerPingMessage)) {
			log.debug(this + " sends " + message);
		}
		super.send(message);
	}

	public void setLastPingTime(long lastPingTime) {
		this.lastPingTime = lastPingTime;
	}

	public long getClientLatency() {
		return clientLatency;
	}

	public ListenerSet getListenerSet() {
		return listenerSet;
	}

	public ServerRoom getRoom() {
		return room;
	}

	public void setRoom(ServerRoom room) {
		this.room = room;
	}

	public ServerGame getGame() {
		return game;
	}

	public void setGame(ServerGame game) {
		this.game = game;
	}
	
	public void setClientLatency(long clientLatency) {
		this.clientLatency = clientLatency;
	}

	public long getLastPingTime() {
		return lastPingTime;
	}

	public boolean isDisconnected() {
		return disconnected;
	}

	public void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected;
	}
	
}
