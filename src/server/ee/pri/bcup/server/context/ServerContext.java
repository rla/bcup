package ee.pri.bcup.server.context;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import ee.pri.bcup.common.Version;
import ee.pri.bcup.common.util.ThreadUtils;
import ee.pri.bcup.server.dao.GameDao;
import ee.pri.bcup.server.dao.PlayerDao;
import ee.pri.bcup.server.dao.RoomDao;
import ee.pri.bcup.server.model.ServerPlayer;

/**
 * Central class for accessing server context.
 * It also distributes the events of player disconnect.
 *
 * @author Raivo Laanemets
 */
public class ServerContext implements InitializingBean {
	private static final Logger log = Logger.getLogger(ServerContext.class);
	
	private List<DisconnectListener> disconnectListeners;
	private RoomManager roomManager;
	private PlayerManager playerManager;
	private ProposeManager proposeManager;
	private AuthenticationManager authenticationManager;
	private PoolManager poolManager;
	private PlayerDao playerDao;
	private GameDao gameDao;
	private RoomDao roomDao;

	public RoomDao getRoomDao() {
		return roomDao;
	}

	@Required
	public void setRoomDao(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	public GameDao getGameDao() {
		return gameDao;
	}

	@Required
	public void setGameDao(GameDao gameDao) {
		this.gameDao = gameDao;
	}

	public PlayerDao getPlayerDao() {
		return playerDao;
	}

	@Required
	public void setPlayerDao(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	@Required
	public void setDisconnectListeners(List<DisconnectListener> disconnectListeners) {
		this.disconnectListeners = disconnectListeners;
	}
	
	public void disconnected(ServerPlayer player) {
		for (DisconnectListener listener : disconnectListeners) {
			listener.disconnected(player);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("server context created");
		log.info("server version: " + Version.VERSION);
	}

	public RoomManager getRoomManager() {
		return roomManager;
	}

	@Required
	public void setRoomManager(RoomManager roomManager) {
		this.roomManager = roomManager;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	@Required
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public ProposeManager getProposeManager() {
		return proposeManager;
	}

	@Required
	public void setProposeManager(ProposeManager proposeManager) {
		this.proposeManager = proposeManager;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	@Required
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public PoolManager getPoolManager() {
		return poolManager;
	}

	@Required
	public void setPoolManager(PoolManager poolManager) {
		this.poolManager = poolManager;
	}
	
	/**
	 * Creates large server debug string. Used for command !debug.
	 */
	public String createDebugString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("PlayerDao:\n").append(playerDao).append('\n');
		builder.append("GameDao:\n").append(gameDao).append('\n');
		builder.append("RoomDao:\n").append(roomDao).append('\n');
		
		builder.append("Threads:\n");
		for (Thread thread : ThreadUtils.getAllThreads()) {
			builder.append(thread).append('\n');
		}
		
		return builder.toString();
	}
	
}
